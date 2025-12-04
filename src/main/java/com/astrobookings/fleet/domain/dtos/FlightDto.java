package com.astrobookings.fleet.domain.dtos;

import com.astrobookings.fleet.domain.models.flight.FlightStatus;

import java.time.LocalDateTime;

public class FlightDto {
    private String id;
    private String rocketId;
    private LocalDateTime departureDate;
    private double basePrice;
    private FlightStatus status;
    private int minPassengers;

    public FlightDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRocketId() {
        return rocketId;
    }

    public void setRocketId(String rocketId) {
        this.rocketId = rocketId;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public int getMinPassengers() {
        return minPassengers;
    }

    public void setMinPassengers(int minPassengers) {
        this.minPassengers = minPassengers;
    }
}
