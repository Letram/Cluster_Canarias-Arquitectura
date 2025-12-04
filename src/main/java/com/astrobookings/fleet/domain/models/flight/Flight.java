package com.astrobookings.fleet.domain.models.flight;

import java.time.LocalDateTime;

public class Flight {
    private String id;
    private String rocketId;
    private FlightDepartureDate departureDate;
    private FlightPrice basePrice;
    private FlightStatus status;
    private FlightPassengers minPassengers;

    public Flight() {
    }

    public Flight(String id, String rocketId, FlightDepartureDate departureDate, FlightPrice basePrice, FlightStatus status,
                  FlightPassengers minPassengers) {
        this.id = id;
        this.rocketId = rocketId;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.status = status;
        this.minPassengers = minPassengers;
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
        return departureDate.value();
    }

    public void setDepartureDate(FlightDepartureDate departureDate) {
        this.departureDate = departureDate;
    }

    public FlightPrice getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(FlightPrice basePrice) {

        this.basePrice = basePrice;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public int getMinPassengers() {
        return minPassengers.value();
    }

    public void setMinPassengers(FlightPassengers minPassengers) {
        this.minPassengers = minPassengers;
    }
}