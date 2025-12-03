package com.astrobookings.fleet.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.FlightService;
import com.astrobookings.fleet.domain.RocketService;
import com.astrobookings.fleet.domain.ports.input.FlightUseCases;
import com.astrobookings.fleet.domain.ports.input.RocketUseCases;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;

public class FleetUseCasesFactory {

    public static RocketUseCases getRocketUseCases(RocketRepositoryPort rrp) {
        return new RocketService(rrp);
    }

    public static FlightUseCases getFlightUseCases(FlightRepositoryPort flp, RocketRepositoryPort rrp) {
        return new FlightService(flp, rrp);
    }

}
