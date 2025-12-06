package com.astrobookings.sales.infrastructure.presentation.models;

public record HTTPBooking(String id, HTTPSalesFlight flight, String passengerName, double finalPrice,
                          String paymentTransactionId) {
}
