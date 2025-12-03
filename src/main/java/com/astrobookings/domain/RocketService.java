package com.astrobookings.domain;

import com.astrobookings.domain.dtos.RocketDto;
import com.astrobookings.domain.models.Rocket;
import com.astrobookings.domain.ports.input.RocketUseCases;
import com.astrobookings.domain.ports.output.RocketRepositoryPort;

import java.util.List;

public class RocketService implements RocketUseCases {

    private final RocketRepositoryPort rocketRepositoryPort;

    public RocketService(RocketRepositoryPort rocketRepositoryPort) {
        this.rocketRepositoryPort = rocketRepositoryPort;
    }

    @Override
    public List<RocketDto> getAllRockets() {
        List<Rocket> rockets = this.rocketRepositoryPort.findAll();

        return rockets.stream().map(this::rocketToDto).toList();
    }

    @Override
    public RocketDto createRocket(RocketDto rocketDto) {

        validateRocket(rocketDto);

        Rocket rocket = dtoToRocket(rocketDto);
        Rocket createdRocket = this.rocketRepositoryPort.save(rocket);
        return rocketToDto(createdRocket);

    }

    private void validateRocket(RocketDto rocketDto) {
        if (rocketDto.getCapacity() > 10) {
            throw new IllegalArgumentException("Rocket capacity over 10");
        }
    }

    private Rocket dtoToRocket(RocketDto rocketDto) {
        Rocket rocket = new Rocket();
        rocket.setId(rocketDto.getId());
        rocket.setName(rocketDto.getName());
        rocket.setCapacity(rocketDto.getCapacity());
        rocket.setSpeed(rocketDto.getSpeed());
        return rocket;
    }

    private RocketDto rocketToDto(Rocket rocket) {
        RocketDto rocketDto = new RocketDto();
        rocketDto.setId(rocket.getId());
        rocketDto.setName(rocket.getName());
        rocketDto.setCapacity(rocket.getCapacity());
        rocketDto.setSpeed(rocket.getSpeed());
        return rocketDto;
    }
}
