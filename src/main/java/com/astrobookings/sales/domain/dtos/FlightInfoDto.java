package com.astrobookings.sales.domain.dtos;

import java.time.LocalDateTime;

public class FlightInfoDto {
    private String id;
    private String rocketId;
    private LocalDateTime departureDate;
    private double basePrice;
    private String status;
    private int minPassengers;
    private int rocketCapacity;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMinPassengers() {
        return minPassengers;
    }

    public void setMinPassengers(int minPassengers) {
        this.minPassengers = minPassengers;
    }
    public int getRocketCapacity() {
        return rocketCapacity;
    }
    public void setRocketCapacity(int rocketCapacity) {
        this.rocketCapacity = rocketCapacity;
    }
}
