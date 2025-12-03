package com.astrobookings.domain.ports.input;

import com.astrobookings.domain.dtos.BookingDto;

import java.util.List;

public interface BookingUseCases {
    BookingDto createBooking(String flightId, String passengerName) throws Exception;

    List<BookingDto> getBookings(String flightId, String passengerName);
}
