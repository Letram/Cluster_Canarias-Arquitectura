package com.astrobookings.sales.domain.models.flight;

public record SalesFlightPrice(double value) {

    public SalesFlightPrice {
        if (value <= 0) throw new IllegalArgumentException("Price cannot be negative or 0");
    }

}
