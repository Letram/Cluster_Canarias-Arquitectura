package com.astrobookings.sales.infrastructure.persistence.adapters;

import com.astrobookings.fleet.domain.models.Flight;
import com.astrobookings.fleet.domain.models.FlightPrice;
import com.astrobookings.fleet.domain.models.FlightStatus;
import com.astrobookings.fleet.domain.models.Rocket;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.sales.domain.dtos.FlightInfoDto;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;

import java.util.List;

public class FleetAdapter implements FlightInfoProvider {
    private final FlightRepositoryPort flightRepositoryPort;
    private final RocketRepositoryPort rocketRepositoryPort;

    public FleetAdapter(FlightRepositoryPort flightRepositoryPort,
                        RocketRepositoryPort rocketRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.rocketRepositoryPort = rocketRepositoryPort;
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

    private FlightInfoDto flightToFlightInfoDto(Flight flight) {
        FlightInfoDto flightInfoDto = new FlightInfoDto();
        flightInfoDto.setId(flight.getId());
        flightInfoDto.setRocketId(flight.getRocketId());
        flightInfoDto.setDepartureDate(flight.getDepartureDate());
        flightInfoDto.setBasePrice(flight.getBasePrice().getPrice());
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
        flight.setDepartureDate(flightInfoDto.getDepartureDate());

        FlightPrice price = new FlightPrice(flightInfoDto.getBasePrice());
        flight.setBasePrice(price);
        flight.setStatus(Enum.valueOf(FlightStatus.class, flightInfoDto.getStatus()));
        flight.setMinPassengers(flightInfoDto.getMinPassengers());
        return flight;
    }
}
