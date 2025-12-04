package com.astrobookings.sales.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.sales.domain.BookingService;
import com.astrobookings.sales.domain.CancellationService;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.domain.ports.input.CancellationUseCases;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;

public class SalesUseCasesFactory {

    public static BookingUseCases getBookingUseCases(BookingRepositoryPort brp, FlightInfoProvider fip) {
        return new BookingService(fip, brp);
    }

    public static CancellationUseCases getCancellationUseCases(FlightInfoProvider fip) {
        return new CancellationService(fip);
    }
}
