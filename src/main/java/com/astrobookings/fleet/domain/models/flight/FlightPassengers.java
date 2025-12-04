package com.astrobookings.fleet.domain.models.flight;

public record FlightPassengers(int value) {

    public FlightPassengers {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid minimum passengers value. Min value >= 1");
        }
    }

}
