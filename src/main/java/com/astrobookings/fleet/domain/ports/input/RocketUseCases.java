package com.astrobookings.fleet.domain.ports.input;

import com.astrobookings.fleet.domain.dtos.RocketDto;

import java.util.List;

public interface RocketUseCases {
    List<RocketDto> getAllRockets();

    RocketDto createRocket(RocketDto rocketDto);
}
