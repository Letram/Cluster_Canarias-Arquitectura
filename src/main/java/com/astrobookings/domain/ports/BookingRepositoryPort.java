package com.astrobookings.domain.ports;

import com.astrobookings.domain.models.Booking;

import java.util.List;

public interface BookingRepositoryPort {
    List<Booking> findAll();

    List<Booking> findByFlightId(String flightId);

    List<Booking> findByPassengerName(String passengerName);

    Booking save(Booking booking);
}
