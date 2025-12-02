package com.astrobookings.presentation.factories;

import com.astrobookings.domain.ports.BookingRepositoryPort;
import com.astrobookings.domain.ports.FlightRepositoryPort;
import com.astrobookings.domain.ports.RocketRepositoryPort;
import com.astrobookings.infrastructure.adapters.InMemoryBookingRepositoryAdapter;
import com.astrobookings.infrastructure.adapters.InMemoryFlightRepositoryAdapter;
import com.astrobookings.infrastructure.adapters.InMemoryRocketRepositoryAdapter;

public class PortFactory {

    private static final BookingRepositoryPort BOOKING_REPOSITORY_PORT = new InMemoryBookingRepositoryAdapter();
    private static final FlightRepositoryPort FLIGHT_REPOSITORY_PORT = new InMemoryFlightRepositoryAdapter();
    private static final RocketRepositoryPort ROCKET_REPOSITORY_PORT = new InMemoryRocketRepositoryAdapter();

    public static BookingRepositoryPort getBookingPort() {
        return BOOKING_REPOSITORY_PORT;
    }

    public static FlightRepositoryPort getFlightPort() {
        return FLIGHT_REPOSITORY_PORT;
    }

    public static RocketRepositoryPort getRocketPort() {
        return ROCKET_REPOSITORY_PORT;
    }

}
