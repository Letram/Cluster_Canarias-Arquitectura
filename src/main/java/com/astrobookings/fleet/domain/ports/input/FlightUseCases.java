package com.astrobookings.fleet.domain.ports.input;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;

import java.util.List;

public interface FlightUseCases {
    List<FleetFlight> getFlights(String statusFilter);

    FleetFlight createFlight(FleetFlight flight);
}
