package com.astrobookings.fleet.domain.ports.output;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;

import java.util.List;

public interface RocketRepositoryPort {
    List<FleetRocket> findAll();

    FleetRocket save(FleetRocket rocket);

    FleetRocket findbyId(String rocketId);
}
