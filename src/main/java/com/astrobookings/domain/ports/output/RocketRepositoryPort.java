package com.astrobookings.domain.ports.output;

import com.astrobookings.domain.models.Rocket;

import java.util.List;

public interface RocketRepositoryPort {
    List<Rocket> findAll();

    Rocket save(Rocket rocket);
}
