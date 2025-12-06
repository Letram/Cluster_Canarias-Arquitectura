package com.astrobookings.sales.domain.ports.output;

import com.astrobookings.sales.domain.models.flight.SalesFlight;

public interface FlightInfoProvider {

    SalesFlight getFlightById(String flightId);

    void markFlightAsSoldOut(String flightId);

    boolean isFlightScheduled(String status);

    void markFlightAsConfirmed(String flightId);

    boolean isFlightCancelled(String status);

    boolean isFlightSoldOut(String status);

    int cancelAllFlights();
}
