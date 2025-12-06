package com.astrobookings.sales.domain.models.booking;

import com.astrobookings.sales.domain.models.flight.SalesFlight;

public class SalesBooking {
    private String id;
    private SalesFlight flight;
    private String passengerName;
    private SalesBookingPrice finalPrice;
    private String paymentTransactionId;

    public SalesBooking() {
    }

    public SalesBooking(String id, String passengerName, SalesBookingPrice finalPrice, String paymentTransactionId) {
        this.id = id;
        this.passengerName = passengerName;
        this.finalPrice = finalPrice;
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public double getFinalPrice() {
        return finalPrice.value();
    }

    public void setFinalPrice(SalesBookingPrice finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public SalesFlight getFlight() {
        return flight;
    }

    public void setFlight(SalesFlight flight) {
        this.flight = flight;
    }
}