package com.astrobookings.fleet.domain.ports.output;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;

import java.util.List;

public interface FlightRepositoryPort {
    List<FleetFlight> findAll();

    List<FleetFlight> findByStatus(String status);

    FleetFlight save(FleetFlight flight);
}
