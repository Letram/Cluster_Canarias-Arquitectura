package com.astrobookings.fleet.infrastructure.persistence.mappers;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.models.rocket.RocketCapacity;
import com.astrobookings.fleet.domain.models.rocket.RocketSpeed;
import com.astrobookings.fleet.infrastructure.persistence.models.DbRocket;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class DbFleetRocketMapper implements DomainMapper<FleetRocket, DbRocket> {

    @Override
    public FleetRocket toDomain(DbRocket infrastructureModel) {
        FleetRocket fleetRocket = new FleetRocket();
        fleetRocket.setId(infrastructureModel.id());
        fleetRocket.setName(infrastructureModel.name());
        RocketCapacity capacity = new RocketCapacity(infrastructureModel.capacity());
        fleetRocket.setCapacity(capacity);
        RocketSpeed speed = new RocketSpeed(infrastructureModel.speed());
        fleetRocket.setSpeed(speed);
        return fleetRocket;
    }

    @Override
    public DbRocket toInfrastructure(FleetRocket domainModel) {
        DbRocket r = new DbRocket(
                domainModel.getId(),
                domainModel.getName(),
                domainModel.getCapacity(),
                domainModel.getSpeed()
        );
        return r;
    }
}
