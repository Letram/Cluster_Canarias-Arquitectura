package com.astrobookings.persistence.factories;

import com.astrobookings.persistence.InMemoryBookingRepository;
import com.astrobookings.persistence.interfaces.BookingRepository;

public class BookingRepositoryFactory {

    public static BookingRepository getBookingRepository() {
        return new InMemoryBookingRepository();
    }

}
