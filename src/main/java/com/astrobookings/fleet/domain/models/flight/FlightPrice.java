package com.astrobookings.fleet.domain.models.flight;

public record FlightPrice(double price) {
    public FlightPrice {
        if (price <= 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}