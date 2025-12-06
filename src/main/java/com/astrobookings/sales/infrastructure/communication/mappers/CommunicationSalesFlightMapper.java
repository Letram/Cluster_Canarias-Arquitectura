package com.astrobookings.sales.infrastructure.communication.mappers;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.models.flight.SalesFlightCapacity;
import com.astrobookings.sales.domain.models.flight.SalesFlightPrice;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class CommunicationSalesFlightMapper implements DomainMapper<SalesFlight, FleetFlight> {

    private final BookingRepositoryPort bookingRepositoryPort;

    public CommunicationSalesFlightMapper(BookingRepositoryPort bookingRepositoryPort) {
        this.bookingRepositoryPort = bookingRepositoryPort;
    }

    @Override
    public SalesFlight toDomain(FleetFlight infrastructureModel) {
        SalesFlight sf = new SalesFlight();

        sf.setId(infrastructureModel.getId());
        sf.setStatus(infrastructureModel.getStatus());
        sf.setDepartureDate(infrastructureModel.getDepartureDate());
        sf.setBasePrice(new SalesFlightPrice(infrastructureModel.getBasePrice()));
        sf.setCapacity(new SalesFlightCapacity(infrastructureModel.getRocket().getCapacity()));

        int passengers = bookingRepositoryPort.findByFlightId(infrastructureModel.getId()).size();
        sf.setCurrentPassengers(passengers);

        return sf;
    }

    @Override
    public FleetFlight toInfrastructure(SalesFlight flight) {
        return null;
    }
}
