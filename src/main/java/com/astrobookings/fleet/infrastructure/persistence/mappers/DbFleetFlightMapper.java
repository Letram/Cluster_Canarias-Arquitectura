package com.astrobookings.fleet.infrastructure.persistence.mappers;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.models.flight.FlightDepartureDate;
import com.astrobookings.fleet.domain.models.flight.FlightPassengers;
import com.astrobookings.fleet.domain.models.flight.FlightPrice;
import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.fleet.infrastructure.persistence.models.DbFlight;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class DbFleetFlightMapper implements DomainMapper<FleetFlight, DbFlight> {

    private final RocketRepositoryPort rocketRepositoryPort;

    public DbFleetFlightMapper(RocketRepositoryPort rocketRepositoryPort) {
        this.rocketRepositoryPort = rocketRepositoryPort;
    }

    @Override
    public FleetFlight toDomain(DbFlight infrastructureModel) {
        FleetFlight ff = new FleetFlight();

        ff.setId(infrastructureModel.id());
        ff.setDepartureDate(new FlightDepartureDate(infrastructureModel.departureDate()));
        ff.setBasePrice(new FlightPrice(infrastructureModel.price()));
        ff.setMinPassengers(new FlightPassengers(infrastructureModel.minPassengers()));

        ff.setStatus(infrastructureModel.status());

        FleetRocket fleetRocket = rocketRepositoryPort.findbyId(infrastructureModel.rocketId());
        ff.setRocket(fleetRocket);

        return ff;
    }

    @Override
    public DbFlight toInfrastructure(FleetFlight domainModel) {

        return new DbFlight(
                domainModel.getId(),
                domainModel.getRocket().getId(),
                domainModel.getDepartureDate(),
                domainModel.getBasePrice(),
                domainModel.getStatus(),
                domainModel.getMinPassengers()
        );
    }
}
