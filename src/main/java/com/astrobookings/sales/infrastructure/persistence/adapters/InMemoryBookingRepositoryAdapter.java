package com.astrobookings.sales.infrastructure.persistence.adapters;

import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryBookingRepositoryAdapter implements BookingRepositoryPort {
    private static final Map<String, SalesBooking> bookings = new HashMap<>();
    private static int nextId = 1;

    @Override
    public List<SalesBooking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public List<SalesBooking> findByFlightId(String flightId) {
        return bookings.values().stream()
                .filter(booking -> booking.getFlight().getId().equals(flightId))
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesBooking> findByPassengerName(String passengerName) {
        return bookings.values().stream()
                .filter(booking -> booking.getPassengerName().equalsIgnoreCase(passengerName))
                .collect(Collectors.toList());
    }

    @Override
    public SalesBooking save(SalesBooking booking) {
        if (booking.getId() == null) {
            booking.setId("b" + nextId++);
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }
}