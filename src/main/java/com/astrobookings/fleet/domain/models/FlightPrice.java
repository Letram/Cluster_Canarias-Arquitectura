package com.astrobookings.fleet.domain.models;

public class FlightPrice {
    private final double price;

    public FlightPrice(Number price) {
        if (price.doubleValue() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price.doubleValue();
    }

    public double getPrice() {
        return price;
    }
}
