package com.astrobookings.fleet.infrastructure.presentation.mappers;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.models.rocket.RocketCapacity;
import com.astrobookings.fleet.domain.models.rocket.RocketSpeed;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPRocket;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class HttpFleetRocketMapper implements DomainMapper<FleetRocket, HTTPRocket> {

    @Override
    public FleetRocket toDomain(HTTPRocket infrastructureModel) {
        FleetRocket fr = new FleetRocket();

        fr.setId(infrastructureModel.id());
        fr.setName(infrastructureModel.name());

        fr.setCapacity(new RocketCapacity(infrastructureModel.capacity()));
        fr.setSpeed(new RocketSpeed(infrastructureModel.speed()));

        return fr;
    }

    @Override
    public HTTPRocket toInfrastructure(FleetRocket domainModel) {
        return new HTTPRocket(
                domainModel.getId(),
                domainModel.getName(),
                domainModel.getCapacity(),
                domainModel.getSpeed()
        );
    }
}
