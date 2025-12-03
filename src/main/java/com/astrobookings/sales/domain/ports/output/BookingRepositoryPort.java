package com.astrobookings.sales.domain.ports.output;

import com.astrobookings.sales.domain.models.Booking;

import java.util.List;

public interface BookingRepositoryPort {
    List<Booking> findAll();

    List<Booking> findByFlightId(String flightId);

    List<Booking> findByPassengerName(String passengerName);

    Booking save(Booking booking);
}
