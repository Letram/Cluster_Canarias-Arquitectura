package com.astrobookings.fleet.infrastructure.persistence.models;

import com.astrobookings.shared.domain.models.FlightStatus;

import java.time.LocalDateTime;

public record DbFlight(String id, String rocketId, LocalDateTime departureDate, double price, FlightStatus status,
                       int minPassengers) {
}
