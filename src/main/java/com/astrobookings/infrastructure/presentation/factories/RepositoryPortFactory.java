package com.astrobookings.infrastructure.presentation.factories;

import com.astrobookings.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.infrastructure.persistence.adapters.InMemoryBookingRepositoryAdapter;
import com.astrobookings.infrastructure.persistence.adapters.InMemoryFlightRepositoryAdapter;
import com.astrobookings.infrastructure.persistence.adapters.InMemoryRocketRepositoryAdapter;

public class RepositoryPortFactory {

    private static final BookingRepositoryPort BOOKING_REPOSITORY_PORT = new InMemoryBookingRepositoryAdapter();
    private static final FlightRepositoryPort FLIGHT_REPOSITORY_PORT = new InMemoryFlightRepositoryAdapter();
    private static final RocketRepositoryPort ROCKET_REPOSITORY_PORT = new InMemoryRocketRepositoryAdapter();

    public static BookingRepositoryPort getBookingRepositoryPort() {
        return BOOKING_REPOSITORY_PORT;
    }

    public static FlightRepositoryPort getFlightRepositoryPort() {
        return FLIGHT_REPOSITORY_PORT;
    }

    public static RocketRepositoryPort getRocketRepositoryPort() {
        return ROCKET_REPOSITORY_PORT;
    }

}
