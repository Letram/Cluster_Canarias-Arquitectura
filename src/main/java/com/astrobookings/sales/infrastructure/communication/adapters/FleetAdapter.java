package com.astrobookings.sales.infrastructure.communication.adapters;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.sales.domain.PaymentGateway;
import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.domain.ports.output.NotificationUseCases;
import com.astrobookings.shared.domain.models.FlightStatus;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FleetAdapter implements FlightInfoProvider {

    private final FlightRepositoryPort flightRepositoryPort;
    private final BookingRepositoryPort bookingRepositoryPort;

    private final DomainMapper<SalesFlight, FleetFlight> domainMapper;

    public FleetAdapter(FlightRepositoryPort flightRepositoryPort,
                        BookingRepositoryPort bookingRepositoryPort,
                        DomainMapper<SalesFlight, FleetFlight> domainMapper) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.domainMapper = domainMapper;
    }

    @Override
    public SalesFlight getFlightById(String flightId) {
        FleetFlight flight = flightRepositoryPort.findAll().stream().filter(f -> f.getId().equals(flightId)).findFirst().orElse(null);

        return domainMapper.toDomain(flight);
    }

    @Override
    public void markFlightAsSoldOut(String flightId) {
        flightRepositoryPort.findAll().stream()
                .filter(f -> f.getId().equals(flightId))
                .findFirst()
                .ifPresent(flight -> {
                    flight.sold();
                    flightRepositoryPort.save(flight);
                });
    }

    @Override
    public boolean isFlightScheduled(String status) {
        return status.equalsIgnoreCase(FlightStatus.SCHEDULED.name());
    }

    @Override
    public void markFlightAsConfirmed(String flightId) {
        flightRepositoryPort.findAll().stream()
                .filter(f -> f.getId().equals(flightId))
                .findFirst()
                .ifPresent(flight -> {
                    flight.confirm();
                    flightRepositoryPort.save(flight);
                });
    }

    @Override
    public boolean isFlightCancelled(String status) {
        return status.equalsIgnoreCase(FlightStatus.CANCELLED.name());
    }

    @Override
    public boolean isFlightSoldOut(String status) {
        return status.equalsIgnoreCase(FlightStatus.SOLD_OUT.name());
    }

    @Override
    public int cancelAllFlights() {
        List<FleetFlight> flights = flightRepositoryPort.findAll();
        int cancelledCount = 0;
        LocalDateTime now = LocalDateTime.now();

        for (FleetFlight flight : flights) {
            if (flight.getStatus() == FlightStatus.SCHEDULED) {
                long daysUntilDeparture = ChronoUnit.DAYS.between(now, flight.getDepartureDate());
                if (daysUntilDeparture <= 7) {
                    List<SalesBooking> bookings = bookingRepositoryPort.findByFlightId(flight.getId());
                    if (bookings.size() < flight.getMinPassengers()) {
                        // Cancel flight
                        System.out.println("[CANCELLATION SERVICE] Cancelling flight " + flight.getId() + " - Only "
                                + bookings.size() + "/5 passengers, departing in " + daysUntilDeparture + " days");
                        flight.cancel();
                        flightRepositoryPort.save(flight);

                        // Refund bookings
                        for (SalesBooking booking : bookings) {
                            PaymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
                        }

                        // Notify
                        NotificationUseCases.notifyCancellation(flight.getId(), bookings);
                        cancelledCount++;
                    }
                }
            }
        }
        return cancelledCount;
    }

}
