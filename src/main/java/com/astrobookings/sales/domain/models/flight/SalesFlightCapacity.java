package com.astrobookings.sales.domain.models.flight;

public record SalesFlightCapacity(int value) {
    public SalesFlightCapacity {
        if (value <= 0) throw new IllegalArgumentException("Capacity cannot be negative or 0");
    }
}
