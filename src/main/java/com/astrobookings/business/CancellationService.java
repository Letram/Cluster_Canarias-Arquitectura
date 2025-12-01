package com.astrobookings.business;

import com.astrobookings.persistence.factories.BookingRepositoryFactory;
import com.astrobookings.persistence.factories.FlightRepositoryFactory;
import com.astrobookings.persistence.interfaces.BookingRepository;
import com.astrobookings.persistence.interfaces.FlightRepository;
import com.astrobookings.persistence.models.Booking;
import com.astrobookings.persistence.models.Flight;
import com.astrobookings.persistence.models.FlightStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CancellationService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public CancellationService() {
        this.flightRepository = FlightRepositoryFactory.getFlightRepository();
        this.bookingRepository = BookingRepositoryFactory.getBookingRepository();
    }

    public String cancelFlights() throws Exception {
        List<Flight> flights = flightRepository.findAll();
        int cancelledCount = 0;
        LocalDateTime now = LocalDateTime.now();

        for (Flight flight : flights) {
            if (flight.getStatus() == FlightStatus.SCHEDULED) {
                long daysUntilDeparture = ChronoUnit.DAYS.between(now, flight.getDepartureDate());
                if (daysUntilDeparture <= 7) {
                    List<Booking> bookings = bookingRepository.findByFlightId(flight.getId());
                    if (bookings.size() < flight.getMinPassengers()) {
                        // Cancel flight
                        System.out.println("[CANCELLATION SERVICE] Cancelling flight " + flight.getId() + " - Only "
                                + bookings.size() + "/5 passengers, departing in " + daysUntilDeparture + " days");
                        flight.setStatus(FlightStatus.CANCELLED);
                        flightRepository.save(flight);

                        // Refund bookings
                        for (Booking booking : bookings) {
                            PaymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
                        }

                        // Notify
                        NotificationService.notifyCancellation(flight.getId(), bookings);
                        cancelledCount++;
                    }
                }
            }
        }

        return "{\"message\": \"Flight cancellation check completed\", \"cancelledFlights\": " + cancelledCount + "}";
    }
}