package com.astrobookings.fleet.infrastructure.presentation.mappers;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.models.flight.FlightDepartureDate;
import com.astrobookings.fleet.domain.models.flight.FlightPassengers;
import com.astrobookings.fleet.domain.models.flight.FlightPrice;
import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPFlight;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPRocket;
import com.astrobookings.shared.domain.models.FlightStatus;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class HttpFleetFlightMapper implements DomainMapper<FleetFlight, HTTPFlight> {

    private final RocketRepositoryPort rocketRepositoryPort;
    private final DomainMapper<FleetRocket, HTTPRocket> rocketMapper;

    public HttpFleetFlightMapper(RocketRepositoryPort rocketRepositoryPort, DomainMapper<FleetRocket, HTTPRocket> rocketMapper) {
        this.rocketRepositoryPort = rocketRepositoryPort;
        this.rocketMapper = rocketMapper;
    }

    @Override
    public FleetFlight toDomain(HTTPFlight infrastructureModel) {
        FleetFlight ff = new FleetFlight();

        ff.setId(infrastructureModel.id());
        ff.setBasePrice(new FlightPrice(infrastructureModel.basePrice()));
        ff.setMinPassengers(new FlightPassengers(infrastructureModel.minPassengers()));
        ff.setStatus(FlightStatus.valueOf(infrastructureModel.status()));
        ff.setDepartureDate(new FlightDepartureDate(infrastructureModel.departureDate()));

        FleetRocket fleetRocket = rocketRepositoryPort.findbyId(infrastructureModel.id());
        ff.setRocket(fleetRocket);

        return ff;
    }

    @Override
    public HTTPFlight toInfrastructure(FleetFlight domainModel) {
        HTTPRocket hr = rocketMapper.toInfrastructure(domainModel.getRocket());
        return new HTTPFlight(
                domainModel.getId(),
                hr,
                domainModel.getDepartureDate(),
                domainModel.getBasePrice(),
                domainModel.getStatus().name(),
                domainModel.getMinPassengers()
        );
    }
}
