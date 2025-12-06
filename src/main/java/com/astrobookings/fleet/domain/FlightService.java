package com.astrobookings.fleet.domain;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.models.flight.FlightPassengers;
import com.astrobookings.fleet.domain.ports.input.FlightUseCases;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.shared.domain.models.FlightStatus;

import java.util.List;

public class FlightService implements FlightUseCases {
    private final FlightRepositoryPort flightRepositoryPort;
    private final RocketRepositoryPort rocketRepositoryPort;

    public FlightService(FlightRepositoryPort flightRepositoryPort, RocketRepositoryPort rocketRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
        this.rocketRepositoryPort = rocketRepositoryPort;
    }

    @Override
    public List<FleetFlight> getFlights(String statusFilter) {
        if (statusFilter != null && !statusFilter.isEmpty()) {
            return flightRepositoryPort.findByStatus(statusFilter).stream().toList();
        } else {
            return flightRepositoryPort.findAll().stream().toList();
        }
    }

    @Override
    public FleetFlight createFlight(FleetFlight flight) {

        // Set defaults
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setMinPassengers(new FlightPassengers(5));

        // Validate
        String error = validateFlight(flight);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        return flightRepositoryPort.save(flight);
    }

    private String validateFlight(FleetFlight flight) {

        // Business validations
        if (rocketRepositoryPort.findAll().stream().noneMatch(r -> r.getId().equals(flight.getRocket().getId()))) {
            return "Rocket with id " + flight.getRocket().getId() + " does not exist";
        }

        return null;
    }

}