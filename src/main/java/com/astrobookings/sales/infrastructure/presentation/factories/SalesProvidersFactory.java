package com.astrobookings.sales.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.infrastructure.persistence.adapters.FleetAdapter;

public class SalesProvidersFactory {

    public static FlightInfoProvider getFlightInfoProvider(FlightRepositoryPort frp, RocketRepositoryPort rrp, BookingRepositoryPort brp) {
        return new FleetAdapter(frp, rrp, brp);
    }
}
