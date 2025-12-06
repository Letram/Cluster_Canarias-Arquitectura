package com.astrobookings.sales.domain.models.booking;

public record SalesBookingPrice(double value) {
    public SalesBookingPrice {
        if (value <= 0) throw new IllegalArgumentException("Price cannot be negative or 0");
    }
}
