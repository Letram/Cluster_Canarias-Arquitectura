package com.astrobookings.sales.infrastructure.presentation.factories;

import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.infrastructure.persistence.adapters.InMemoryBookingRepositoryAdapter;

public class SalesRepositoryPortFactory {
    private static final BookingRepositoryPort BOOKING_REPOSITORY_PORT = new InMemoryBookingRepositoryAdapter();

    public static BookingRepositoryPort getBookingRepositoryPort() {
        return BOOKING_REPOSITORY_PORT;
    }

}
