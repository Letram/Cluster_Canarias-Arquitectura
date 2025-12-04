package com.astrobookings.fleet.domain;

import com.astrobookings.fleet.domain.dtos.FlightDto;
import com.astrobookings.fleet.domain.models.flight.*;
import com.astrobookings.fleet.domain.ports.input.FlightUseCases;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;

import java.util.List;

public class FlightService implements FlightUseCases {
    private final FlightRepositoryPort flightRepositoryPort;
    private final RocketRepositoryPort rocketRepositoryPort;

    public FlightService(FlightRepositoryPort flightRepositoryPort, RocketRepositoryPort rocketRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.rocketRepositoryPort = rocketRepositoryPort;
    }

    @Override
    public List<FlightDto> getFlights(String statusFilter) {
        if (statusFilter != null && !statusFilter.isEmpty()) {
            return flightRepositoryPort.findByStatus(statusFilter).stream().map(this::flightToDto).toList();
        } else {
            return flightRepositoryPort.findAll().stream().map(this::flightToDto).toList();
        }
    }

    @Override
    public FlightDto createFlight(FlightDto flightDto) {

        // dto -> model
        Flight flight = dtoToFlight(flightDto);

        // Set defaults
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setMinPassengers(new FlightPassengers(5));

        // Validate
        String error = validateFlight(flight);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Save
        Flight savedFlight = flightRepositoryPort.save(flight);
        return flightToDto(savedFlight);
    }

    private String validateFlight(Flight flight) {

        // Business validations
        if (rocketRepositoryPort.findAll().stream().noneMatch(r -> r.getId().equals(flight.getRocketId()))) {
            return "Rocket with id " + flight.getRocketId() + " does not exist";
        }

        return null;
    }

    private FlightDto flightToDto(Flight flight) {
        FlightDto flightDto = new FlightDto();
        flightDto.setId(flight.getId());
        flightDto.setRocketId(flight.getRocketId());
        flightDto.setDepartureDate(flight.getDepartureDate());
        flightDto.setBasePrice(flight.getBasePrice().price());
        flightDto.setMinPassengers(flight.getMinPassengers());
        flightDto.setStatus(flight.getStatus());
        return flightDto;
    }

    private Flight dtoToFlight(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setId(flightDto.getId());
        flight.setRocketId(flightDto.getRocketId());
        flight.setDepartureDate(new FlightDepartureDate(flightDto.getDepartureDate()));
        FlightPrice price = new FlightPrice(flightDto.getBasePrice());
        flight.setBasePrice(price);
        flight.setMinPassengers(new FlightPassengers(flightDto.getMinPassengers()));
        flight.setStatus(flightDto.getStatus());
        return flight;
    }


}