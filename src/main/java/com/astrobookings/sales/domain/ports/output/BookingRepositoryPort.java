package com.astrobookings.sales.domain.ports.output;

import com.astrobookings.sales.domain.models.booking.SalesBooking;

import java.util.List;

public interface BookingRepositoryPort {
    List<SalesBooking> findAll();

    List<SalesBooking> findByFlightId(String flightId);

    List<SalesBooking> findByPassengerName(String passengerName);

    SalesBooking save(SalesBooking booking);
}
