package com.astrobookings.fleet.infrastructure.presentation.models;

import java.time.LocalDateTime;

public record HTTPFlight(String id, HTTPRocket rocket, LocalDateTime departureDate, double basePrice, String status,
                         int minPassengers) {
}
