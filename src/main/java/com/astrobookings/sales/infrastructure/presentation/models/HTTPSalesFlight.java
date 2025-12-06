package com.astrobookings.sales.infrastructure.presentation.models;

import java.time.LocalDateTime;

public record HTTPSalesFlight(String id, LocalDateTime departureDate, double basePrice, int minPassengers, int capacity, int currentPassengers, String status) {
}
