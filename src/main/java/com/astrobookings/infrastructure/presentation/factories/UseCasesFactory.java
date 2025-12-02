package com.astrobookings.infrastructure.presentation.factories;

import com.astrobookings.domain.FlightService;
import com.astrobookings.domain.RocketService;
import com.astrobookings.domain.ports.input.FlightUseCases;
import com.astrobookings.domain.ports.input.RocketUseCases;
import com.astrobookings.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.domain.ports.output.RocketRepositoryPort;

public class UseCasesFactory {

    public static RocketUseCases getRocketUseCases(RocketRepositoryPort rrp) {
        return new RocketService(rrp);
    }
    public static FlightUseCases getFlightUseCases(FlightRepositoryPort flp, RocketRepositoryPort rrp) {
        return new FlightService(flp, rrp);
    }
}
