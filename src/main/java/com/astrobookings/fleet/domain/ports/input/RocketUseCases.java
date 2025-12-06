package com.astrobookings.fleet.domain.ports.input;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;

import java.util.List;

public interface RocketUseCases {
    List<FleetRocket> getAllRockets();

    FleetRocket createRocket(FleetRocket rocketDto);
}
