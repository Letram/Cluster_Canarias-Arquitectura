package com.astrobookings.fleet.domain;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.ports.input.RocketUseCases;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;

import java.util.List;

public class RocketService implements RocketUseCases {

    private final RocketRepositoryPort rocketRepositoryPort;

    public RocketService(RocketRepositoryPort rocketRepositoryPort) {
        this.rocketRepositoryPort = rocketRepositoryPort;
    }

    @Override
    public List<FleetRocket> getAllRockets() {
        List<FleetRocket> rockets = this.rocketRepositoryPort.findAll();

        return rockets.stream().toList();
    }

    @Override
    public FleetRocket createRocket(FleetRocket rocket) {
        return this.rocketRepositoryPort.save(rocket);
    }

}
