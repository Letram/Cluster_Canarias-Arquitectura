package com.astrobookings.fleet.infrastructure.persistence.adapters;

import com.astrobookings.fleet.domain.models.Rocket;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRocketRepositoryAdapter implements RocketRepositoryPort {
    private static final Map<String, Rocket> rockets = new HashMap<>();
    private static int nextId;

    static {
        // Pre-load one rocket
        var rocketId = "00000000-0000-0000-0000-000000000001";
        Rocket falcon9 = new Rocket(rocketId, "Falcon 9", 7, 27000.0);
        rockets.put(rocketId, falcon9);
        nextId = 2;
    }

    @Override
    public List<Rocket> findAll() {
        return new ArrayList<>(rockets.values());
    }

    @Override
    public Rocket save(Rocket rocket) {
        if (rocket.getId() == null) {
            rocket.setId("r" + nextId++);
        }
        rockets.put(rocket.getId(), rocket);
        return rocket;
    }
}