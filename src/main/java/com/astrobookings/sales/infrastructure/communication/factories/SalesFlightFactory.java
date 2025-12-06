package com.astrobookings.sales.infrastructure.communication.factories;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.infrastructure.communication.mappers.CommunicationSalesFlightMapper;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class SalesFlightFactory {
    public static DomainMapper<SalesFlight, FleetFlight> getSalesFlightMapper(BookingRepositoryPort brp) {
        return new CommunicationSalesFlightMapper(brp);
    }
}
