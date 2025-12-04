package com.astrobookings.fleet.domain.ports.output;

import com.astrobookings.fleet.domain.models.rocket.Rocket;

import java.util.List;

public interface RocketRepositoryPort {
    List<Rocket> findAll();

    Rocket save(Rocket rocket);
}
