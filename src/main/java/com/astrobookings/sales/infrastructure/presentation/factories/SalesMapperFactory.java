package com.astrobookings.sales.infrastructure.presentation.factories;

import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.infrastructure.presentation.mappers.HttpSalesBookingMapper;
import com.astrobookings.sales.infrastructure.presentation.mappers.HttpSalesFlightMapper;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPBooking;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPSalesFlight;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class SalesMapperFactory {

    public static DomainMapper<SalesBooking, HTTPBooking> getSalesBookingHandlerMapper(DomainMapper<SalesFlight, HTTPSalesFlight> flightDomainMapper) {
        return new HttpSalesBookingMapper(flightDomainMapper);
    }

    public static DomainMapper<SalesFlight, HTTPSalesFlight> getSalesFlightHTTPMapper(){
        return new HttpSalesFlightMapper();
    }
}
