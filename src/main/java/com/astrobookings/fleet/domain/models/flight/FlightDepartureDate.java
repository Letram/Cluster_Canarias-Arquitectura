package com.astrobookings.fleet.domain.models.flight;

import java.time.LocalDateTime;

public record FlightDepartureDate(LocalDateTime value) {
    public FlightDepartureDate {
        LocalDateTime now = LocalDateTime.now();
        if (!value.isAfter(now)) {
            throw new IllegalArgumentException("Departure date must be in the future");
        }

        LocalDateTime oneYearAhead = now.plusYears(1);
        if (value.isAfter(oneYearAhead)) {
            throw new IllegalArgumentException("Departure date cannot be more than 1 year ahead");
        }

    }
}
