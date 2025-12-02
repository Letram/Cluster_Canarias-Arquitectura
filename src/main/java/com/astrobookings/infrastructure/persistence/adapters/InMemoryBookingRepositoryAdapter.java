package com.astrobookings.infrastructure.persistence.adapters;

import com.astrobookings.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.domain.models.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryBookingRepositoryAdapter implements BookingRepositoryPort {
    private static final Map<String, Booking> bookings = new HashMap<>();
    private static int nextId = 1;

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public List<Booking> findByFlightId(String flightId) {
        return bookings.values().stream()
                .filter(booking -> booking.getFlightId().equals(flightId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByPassengerName(String passengerName) {
        return bookings.values().stream()
                .filter(booking -> booking.getPassengerName().equalsIgnoreCase(passengerName))
                .collect(Collectors.toList());
    }

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId("b" + nextId++);
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }
}