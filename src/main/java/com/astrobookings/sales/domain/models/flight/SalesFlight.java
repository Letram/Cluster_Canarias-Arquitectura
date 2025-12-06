package com.astrobookings.sales.domain.models.flight;

import com.astrobookings.shared.domain.models.FlightStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SalesFlight {
    private String id;
    private LocalDateTime departureDate;
    private SalesFlightPrice basePrice;
    private SalesFlightPassengers minPassengers;
    private SalesFlightCapacity capacity;
    private int currentPassengers;
    private FlightStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBasePrice() {
        return basePrice.value();
    }

    public void setBasePrice(SalesFlightPrice basePrice) {
        this.basePrice = basePrice;
    }

    public int getMinPassengers() {
        return minPassengers.value();
    }

    public void setMinPassengers(SalesFlightPassengers minPassengers) {
        this.minPassengers = minPassengers;
    }

    public int getCapacity() {
        return capacity.value();
    }

    public void setCapacity(SalesFlightCapacity capacity) {
        this.capacity = capacity;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    public void setCurrentPassengers(int currentPassengers) {
        this.currentPassengers = currentPassengers;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public boolean isAvailableForBooking() {
        boolean hasValidStatus = status.equals(FlightStatus.SCHEDULED) || status.equals(FlightStatus.CONFIRMED);

        boolean hasAvailableSeats = currentPassengers < capacity.value();

        return hasValidStatus && hasAvailableSeats;
    }

    public double getFinalPrice() {
        return getBasePrice() - calculateDiscount();
    }

    private double calculateDiscount() {
        LocalDateTime now = LocalDateTime.now();
        long daysUntilDeparture = ChronoUnit.DAYS.between(now, getDepartureDate());

        // Precedence: only one discount
        if (currentPassengers + 1 == getCapacity()) {
            return 0.0; // Last seat, no discount
        } else if (currentPassengers + 1 == getMinPassengers()) {
            return 0.3; // One short of min, 30% off
        } else if (daysUntilDeparture > 180) {
            return 0.1; // >6 months, 10% off
        } else if (daysUntilDeparture >= 7 && daysUntilDeparture <= 30) {
            return 0.2; // 1 month to 1 week, 20% off
        } else {
            return 0.0; // No discount
        }
    }

    public boolean canBeMarkedAsSoldOut() {
        return currentPassengers == getCapacity();
    }

    public boolean canBeConfirmed() {
        return currentPassengers >= getMinPassengers() && status.equals(FlightStatus.SCHEDULED);
    }
}
