package com.astrobookings.sales.domain.models.flight;

public record SalesFlightPassengers(int value) {
    public SalesFlightPassengers {
        if (value <= 0) throw new IllegalArgumentException("Passengers cannot be negative or 0");
    }
}
