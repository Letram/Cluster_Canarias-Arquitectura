package com.astrobookings.fleet.domain.models.flight;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.shared.domain.models.FlightStatus;

import java.time.LocalDateTime;

public class FleetFlight {
    private String id;
    private FleetRocket rocket;
    private FlightDepartureDate departureDate;
    private FlightPrice basePrice;
    private FlightStatus status;
    private FlightPassengers minPassengers;

    public FleetFlight() {
    }

    public FleetFlight(String id, FlightDepartureDate departureDate, FlightPrice basePrice, FlightStatus status,
                       FlightPassengers minPassengers) {
        this.id = id;
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

    public LocalDateTime getDepartureDate() {
        return departureDate.value();
    }

    public void setDepartureDate(FlightDepartureDate departureDate) {
        this.departureDate = departureDate;
    }

    public double getBasePrice() {
        return basePrice.price();
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

    public FleetRocket getRocket() {
        return rocket;
    }

    public void setRocket(FleetRocket rocket) {
        this.rocket = rocket;
    }

    public void cancel() {
        this.status = FlightStatus.CANCELLED;
    }

    public void sold() {
        this.status = FlightStatus.SOLD_OUT;
    }

    public void confirm() {
        this.status = FlightStatus.CONFIRMED;
    }
}