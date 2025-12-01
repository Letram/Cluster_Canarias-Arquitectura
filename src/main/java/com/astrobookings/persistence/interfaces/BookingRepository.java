package com.astrobookings.persistence.interfaces;

import com.astrobookings.persistence.models.Booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> findAll();

    List<Booking> findByFlightId(String flightId);

    List<Booking> findByPassengerName(String passengerName);

    Booking save(Booking booking);
}
