package com.astrobookings.sales.infrastructure.persistence.models;

public record Booking(String id, String flightId, String passengerName, double finalPrice,
                      String paymentTransactionId) {
}