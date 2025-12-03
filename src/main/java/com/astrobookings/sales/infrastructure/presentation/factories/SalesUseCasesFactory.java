package com.astrobookings.sales.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.sales.domain.BookingService;
import com.astrobookings.sales.domain.CancellationService;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.domain.ports.input.CancellationUseCases;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;

public class SalesUseCasesFactory {

    public static BookingUseCases getBookingUseCases(BookingRepositoryPort brp, FlightRepositoryPort frp, RocketRepositoryPort rrp) {
        return new BookingService(brp, frp, rrp);
    }

    public static CancellationUseCases getCancellationUseCases(FlightRepositoryPort frp, BookingRepositoryPort brp) {
        return new CancellationService(frp, brp);
    }
}
