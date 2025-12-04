package com.astrobookings.sales.infrastructure.persistence.adapters;

import com.astrobookings.fleet.domain.models.flight.*;
import com.astrobookings.fleet.domain.models.rocket.Rocket;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.sales.domain.PaymentGateway;
import com.astrobookings.sales.domain.dtos.FlightInfoDto;
import com.astrobookings.sales.domain.models.Booking;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.domain.ports.output.NotificationUseCases;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FleetAdapter implements FlightInfoProvider {
    private final FlightRepositoryPort flightRepositoryPort;
    private final RocketRepositoryPort rocketRepositoryPort;
    private final BookingRepositoryPort bookingRepositoryPort;

    public FleetAdapter(FlightRepositoryPort flightRepositoryPort,
                        RocketRepositoryPort rocketRepositoryPort,
                        BookingRepositoryPort bookingRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.rocketRepositoryPort = rocketRepositoryPort;
        this.bookingRepositoryPort = bookingRepositoryPort;
    }

    @Override
    public FlightInfoDto getFlightById(String flightId) {
        Flight flight = flightRepositoryPort.findAll().stream().filter(f -> f.getId().equals(flightId)).findFirst().orElse(null);
        return flight != null ? flightToFlightInfoDto(flight) : null;
    }

    @Override
    public void markFlightAsSoldOut(String flightId) {
        flightRepositoryPort.findAll().stream()
                .filter(f -> f.getId().equals(flightId))
                .findFirst()
                .ifPresent(flight -> {
                    flight.setStatus(FlightStatus.SOLD_OUT);
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
                    flight.setStatus(FlightStatus.CONFIRMED);
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
        return cancelledCount;
    }

    private FlightInfoDto flightToFlightInfoDto(Flight flight) {
        FlightInfoDto flightInfoDto = new FlightInfoDto();
        flightInfoDto.setId(flight.getId());
        flightInfoDto.setRocketId(flight.getRocketId());
        flightInfoDto.setDepartureDate(flight.getDepartureDate());
        flightInfoDto.setBasePrice(flight.getBasePrice().price());
        flightInfoDto.setStatus(flight.getStatus().name());
        flightInfoDto.setMinPassengers(flight.getMinPassengers());


        Rocket rocket = rocketRepositoryPort.findAll().stream().filter(r -> r.getId().equals(flight.getRocketId())).findFirst().orElse(null);
        flightInfoDto.setRocketCapacity(rocket.getCapacity());
        return flightInfoDto;
    }

    private Flight dtoToFlight(FlightInfoDto flightInfoDto) {
        Flight flight = new Flight();
        flight.setId(flightInfoDto.getId());
        flight.setRocketId(flightInfoDto.getRocketId());
        flight.setDepartureDate(new FlightDepartureDate(flightInfoDto.getDepartureDate()));

        FlightPrice price = new FlightPrice(flightInfoDto.getBasePrice());
        flight.setBasePrice(price);
        flight.setStatus(Enum.valueOf(FlightStatus.class, flightInfoDto.getStatus()));
        flight.setMinPassengers(new FlightPassengers(flightInfoDto.getMinPassengers()));
        return flight;
    }
}
