package com.astrobookings.domain;

import com.astrobookings.domain.models.Booking;
import com.astrobookings.domain.models.Flight;
import com.astrobookings.domain.models.FlightStatus;
import com.astrobookings.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.domain.ports.output.NotificationUseCases;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CancellationService implements com.astrobookings.domain.ports.input.CancellationUseCases {
    private final FlightRepositoryPort flightRepositoryPort;
    private final BookingRepositoryPort bookingRepositoryPort;

    public CancellationService(FlightRepositoryPort flightRepositoryPort, BookingRepositoryPort bookingRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.bookingRepositoryPort = bookingRepositoryPort;
    }

    @Override
    public String cancelFlights() throws Exception {
        List<Flight> flights = flightRepositoryPort.findAll();
        int cancelledCount = 0;
        LocalDateTime now = LocalDateTime.now();

        for (Flight flight : flights) {
            if (flight.getStatus() == FlightStatus.SCHEDULED) {
                long daysUntilDeparture = ChronoUnit.DAYS.between(now, flight.getDepartureDate());
                if (daysUntilDeparture <= 7) {
                    List<Booking> bookings = bookingRepositoryPort.findByFlightId(flight.getId());
                    if (bookings.size() < flight.getMinPassengers()) {
                        // Cancel flight
                        System.out.println("[CANCELLATION SERVICE] Cancelling flight " + flight.getId() + " - Only "
                                + bookings.size() + "/5 passengers, departing in " + daysUntilDeparture + " days");
                        flight.setStatus(FlightStatus.CANCELLED);
                        flightRepositoryPort.save(flight);

                        // Refund bookings
                        for (Booking booking : bookings) {
                            PaymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
                        }

                        // Notify
                        NotificationUseCases.notifyCancellation(flight.getId(), bookings);
                        cancelledCount++;
                    }
                }
            }
        }

        return "{\"message\": \"Flight cancellation check completed\", \"cancelledFlights\": " + cancelledCount + "}";
    }
}