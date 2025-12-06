package com.astrobookings.sales.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.infrastructure.communication.adapters.FleetAdapter;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class SalesProvidersFactory {

    public static FlightInfoProvider getFlightInfoProvider(FlightRepositoryPort frp, BookingRepositoryPort brp, DomainMapper<SalesFlight, FleetFlight> mapper) {
        return new FleetAdapter(frp, brp, mapper);
    }
}
