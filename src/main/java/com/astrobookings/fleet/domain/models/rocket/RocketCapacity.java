package com.astrobookings.fleet.domain.models.rocket;

public record RocketCapacity(int value) {

    public RocketCapacity {
        if (value > 10) {
            throw new IllegalArgumentException("Rocket capacity over 10");
        }
    }

}
