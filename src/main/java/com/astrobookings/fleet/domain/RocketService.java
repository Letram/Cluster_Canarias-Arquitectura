package com.astrobookings.fleet.domain;

import com.astrobookings.fleet.domain.dtos.RocketDto;
import com.astrobookings.fleet.domain.models.rocket.Rocket;
import com.astrobookings.fleet.domain.models.rocket.RocketCapacity;
import com.astrobookings.fleet.domain.models.rocket.RocketSpeed;
import com.astrobookings.fleet.domain.ports.input.RocketUseCases;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;

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
        Rocket rocket = dtoToRocket(rocketDto);
        Rocket createdRocket = this.rocketRepositoryPort.save(rocket);
        return rocketToDto(createdRocket);
    }

    private Rocket dtoToRocket(RocketDto rocketDto) {
        Rocket rocket = new Rocket();
        rocket.setId(rocketDto.getId());
        rocket.setName(rocketDto.getName());
        rocket.setCapacity(new RocketCapacity(rocketDto.getCapacity()));
        rocket.setSpeed(new RocketSpeed(rocketDto.getSpeed()));
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
