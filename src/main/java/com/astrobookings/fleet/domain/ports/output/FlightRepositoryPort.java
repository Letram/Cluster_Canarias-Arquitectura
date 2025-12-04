package com.astrobookings.fleet.domain.ports.output;

import com.astrobookings.fleet.domain.models.flight.Flight;

import java.util.List;

public interface FlightRepositoryPort {
    List<Flight> findAll();

    List<Flight> findByStatus(String status);

    Flight save(Flight flight);
}
