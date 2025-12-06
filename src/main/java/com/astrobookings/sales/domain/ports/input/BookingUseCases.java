package com.astrobookings.sales.domain.ports.input;

import com.astrobookings.sales.domain.models.booking.SalesBooking;

import java.util.List;

public interface BookingUseCases {
    SalesBooking createBooking(String flightId, String passengerName) throws Exception;

    List<SalesBooking> getBookings(String flightId, String passengerName);
}
