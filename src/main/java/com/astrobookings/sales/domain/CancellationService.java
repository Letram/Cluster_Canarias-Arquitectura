package com.astrobookings.sales.domain;

import com.astrobookings.sales.domain.ports.input.CancellationUseCases;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;

public class CancellationService implements CancellationUseCases {

    private final FlightInfoProvider flightInfoProvider;

    public CancellationService(FlightInfoProvider fip) {
        this.flightInfoProvider = fip;
    }

    @Override
    public String cancelFlights() throws Exception {

        int cancelledCount = flightInfoProvider.cancelAllFlights();

        return "{\"message\": \"Flight cancellation check completed\", \"cancelledFlights\": " + cancelledCount + "}";
    }
}