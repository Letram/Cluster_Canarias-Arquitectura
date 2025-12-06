package com.astrobookings.sales.infrastructure.presentation.mappers;

import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.models.booking.SalesBookingPrice;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPBooking;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPSalesFlight;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class HttpSalesBookingMapper implements DomainMapper<SalesBooking, HTTPBooking> {

    private final DomainMapper<SalesFlight, HTTPSalesFlight> flightDomainMapper;

    public HttpSalesBookingMapper(DomainMapper<SalesFlight, HTTPSalesFlight> flightDomainMapper) {
        this.flightDomainMapper = flightDomainMapper;
    }

    @Override
    public SalesBooking toDomain(HTTPBooking infrastructureModel) {
        SalesBooking salesBooking = new SalesBooking();

        salesBooking.setId(infrastructureModel.id());
        salesBooking.setPassengerName(infrastructureModel.passengerName());
        salesBooking.setPaymentTransactionId(infrastructureModel.paymentTransactionId());
        salesBooking.setFinalPrice(new SalesBookingPrice(infrastructureModel.finalPrice()));
        salesBooking.setFlight(flightDomainMapper.toDomain(infrastructureModel.flight()));

        return salesBooking;
    }

    @Override
    public HTTPBooking toInfrastructure(SalesBooking domainModel) {

        HTTPSalesFlight httpSalesFlight = flightDomainMapper.toInfrastructure(domainModel.getFlight());

        return new HTTPBooking(
                domainModel.getId(),
                httpSalesFlight,
                domainModel.getPassengerName(),
                domainModel.getFinalPrice(),
                domainModel.getPaymentTransactionId()
        );
    }
}
