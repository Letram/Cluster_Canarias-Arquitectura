package com.astrobookings.fleet.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.fleet.infrastructure.persistence.adapters.InMemoryFlightRepositoryAdapter;
import com.astrobookings.fleet.infrastructure.persistence.adapters.InMemoryRocketRepositoryAdapter;

public class FleetRepositoryPortFactory {
    private static final FlightRepositoryPort FLIGHT_REPOSITORY_PORT = new InMemoryFlightRepositoryAdapter();
    private static final RocketRepositoryPort ROCKET_REPOSITORY_PORT = new InMemoryRocketRepositoryAdapter();

    public static FlightRepositoryPort getFlightRepositoryPort() {
        return FLIGHT_REPOSITORY_PORT;
    }

    public static RocketRepositoryPort getRocketRepositoryPort() {
        return ROCKET_REPOSITORY_PORT;
    }

}
