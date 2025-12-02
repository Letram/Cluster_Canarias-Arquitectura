package com.astrobookings.domain.ports.input;

import com.astrobookings.domain.dtos.RocketDto;

import java.util.List;

public interface RocketUseCases {
    List<RocketDto> getAllRockets();

    RocketDto createRocket(RocketDto rocketDto);
}
