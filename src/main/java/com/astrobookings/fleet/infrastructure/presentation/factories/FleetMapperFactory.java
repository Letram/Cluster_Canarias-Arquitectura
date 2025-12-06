package com.astrobookings.fleet.infrastructure.presentation.factories;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.fleet.infrastructure.presentation.mappers.HttpFleetFlightMapper;
import com.astrobookings.fleet.infrastructure.presentation.mappers.HttpFleetRocketMapper;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPFlight;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPRocket;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;

public class FleetMapperFactory {

    public static DomainMapper<FleetFlight, HTTPFlight> getFlightHandlerMapper(RocketRepositoryPort rrp, DomainMapper<FleetRocket, HTTPRocket> domainMapper) {
        return new HttpFleetFlightMapper(rrp, domainMapper);
    }

    public static DomainMapper<FleetRocket, HTTPRocket> getRocketHandlerMapper() {
        return new HttpFleetRocketMapper();
    }
}
