package com.astrobookings.domain.ports.input;

import com.astrobookings.domain.dtos.FlightDto;

import java.util.List;

public interface FlightUseCases {
    List<FlightDto> getFlights(String statusFilter);

    FlightDto createFlight(FlightDto flightDto);
}
