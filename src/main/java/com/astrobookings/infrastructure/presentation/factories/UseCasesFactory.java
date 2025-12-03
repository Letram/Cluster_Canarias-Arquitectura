package com.astrobookings.infrastructure.presentation.factories;

import com.astrobookings.domain.BookingService;
import com.astrobookings.domain.CancellationService;
import com.astrobookings.domain.FlightService;
import com.astrobookings.domain.RocketService;
import com.astrobookings.domain.ports.input.BookingUseCases;
import com.astrobookings.domain.ports.input.CancellationUseCases;
import com.astrobookings.domain.ports.input.FlightUseCases;
import com.astrobookings.domain.ports.input.RocketUseCases;
import com.astrobookings.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.domain.ports.output.RocketRepositoryPort;

public class UseCasesFactory {

    public static RocketUseCases getRocketUseCases(RocketRepositoryPort rrp) {
        return new RocketService(rrp);
    }

    public static FlightUseCases getFlightUseCases(FlightRepositoryPort flp, RocketRepositoryPort rrp) {
        return new FlightService(flp, rrp);
    }

    public static BookingUseCases getBookingUseCases(BookingRepositoryPort brp, FlightRepositoryPort frp, RocketRepositoryPort rrp) {
        return new BookingService(brp, frp, rrp);
    }

    public static CancellationUseCases getCancellationUseCases(FlightRepositoryPort frp, BookingRepositoryPort brp) {
        return new CancellationService(frp, brp);
    }
}
