package com.astrobookings.fleet.infrastructure.persistence.adapters;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.models.rocket.RocketCapacity;
import com.astrobookings.fleet.domain.models.rocket.RocketSpeed;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRocketRepositoryAdapter implements RocketRepositoryPort {
    private static final Map<String, FleetRocket> rockets = new HashMap<>();
    private static int nextId;

    static {
        // Pre-load one rocket
        var rocketId = "00000000-0000-0000-0000-000000000001";
        FleetRocket falcon9 = new FleetRocket(rocketId, "Falcon 9", new RocketCapacity(7), new RocketSpeed(27000.0));
        rockets.put(rocketId, falcon9);
        nextId = 2;
    }

    @Override
    public List<FleetRocket> findAll() {
        return new ArrayList<>(rockets.values());
    }

    @Override
    public FleetRocket save(FleetRocket rocket) {
        if (rocket.getId() == null) {
            rocket.setId("r" + nextId++);
        }
        rockets.put(rocket.getId(), rocket);
        return rocket;
    }

    @Override
    public FleetRocket findbyId(String rocketId) {
        return rockets.get(rocketId);
    }
}