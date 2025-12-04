package com.astrobookings.fleet.domain.models.rocket;

public record RocketSpeed(double value) {

    public RocketSpeed {
        if (value <= 0) {
            throw new IllegalArgumentException("Speed cannot too low.");
        }
    }
}
