package com.astrobookings.sales.infrastructure.presentation.mappers;

import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.models.flight.SalesFlightCapacity;
import com.astrobookings.sales.domain.models.flight.SalesFlightPassengers;
import com.astrobookings.sales.domain.models.flight.SalesFlightPrice;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPSalesFlight;
import com.astrobookings.shared.domain.models.FlightStatus;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class HttpSalesFlightMapper implements DomainMapper<SalesFlight, HTTPSalesFlight> {
    @Override
    public SalesFlight toDomain(HTTPSalesFlight infrastructureModel) {
        SalesFlight sf = new SalesFlight();

        sf.setId(infrastructureModel.id());
        sf.setDepartureDate(infrastructureModel.departureDate());
        sf.setStatus(FlightStatus.valueOf(infrastructureModel.status()));
        sf.setCurrentPassengers(infrastructureModel.currentPassengers());

        sf.setMinPassengers(new SalesFlightPassengers(infrastructureModel.minPassengers()));
        sf.setCapacity(new SalesFlightCapacity(infrastructureModel.capacity()));
        sf.setBasePrice(new SalesFlightPrice(infrastructureModel.basePrice()));

        return sf;
    }

    @Override
    public HTTPSalesFlight toInfrastructure(SalesFlight domainModel) {

        return new HTTPSalesFlight(
                domainModel.getId(),
                domainModel.getDepartureDate(),
                domainModel.getBasePrice(),
                domainModel.getMinPassengers(),
                domainModel.getCapacity(),
                domainModel.getCurrentPassengers(),
                domainModel.getStatus().name()
        );

    }
}
