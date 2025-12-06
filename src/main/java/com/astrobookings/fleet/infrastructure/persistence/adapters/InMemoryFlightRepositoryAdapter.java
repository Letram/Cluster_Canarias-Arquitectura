package com.astrobookings.fleet.infrastructure.persistence.adapters;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.models.flight.FlightDepartureDate;
import com.astrobookings.fleet.domain.models.flight.FlightPassengers;
import com.astrobookings.fleet.domain.models.flight.FlightPrice;
import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.models.rocket.RocketCapacity;
import com.astrobookings.fleet.domain.models.rocket.RocketSpeed;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.shared.domain.models.FlightStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryFlightRepositoryAdapter implements FlightRepositoryPort {
    private static final Map<String, FleetFlight> flights = new HashMap<>();
    private static int nextId;

    static {
        var rocketId = "00000000-0000-0000-0000-000000000001";
        FleetRocket falcon9 = new FleetRocket(rocketId, "Falcon 9", new RocketCapacity(7), new RocketSpeed(27000.0));

        // Pre-load flights
        var flight1Id = "10000000-0000-0000-0000-000000000001";
        FleetFlight flight1 = new FleetFlight(flight1Id, new FlightDepartureDate(LocalDateTime.of(2026, 6, 1, 10, 0)),
                new FlightPrice(1000.0), FlightStatus.SCHEDULED, new FlightPassengers(5));
        flight1.setRocket(falcon9);

        flights.put(flight1Id, flight1);

        var flight2Id = "10000000-0000-0000-0000-000000000002";
        FleetFlight flight2 = new FleetFlight(flight2Id, new FlightDepartureDate(LocalDateTime.of(2026, 12, 1, 10, 0)),
                new FlightPrice(2000.0), FlightStatus.CANCELLED, new FlightPassengers(5));
        flight2.setRocket(falcon9);
        flights.put(flight2Id, flight2);

        nextId = 3;
    }

    @Override
    public List<FleetFlight> findAll() {
        LocalDateTime now = LocalDateTime.now();
        return flights.values().stream()
                .filter(flight -> flight.getDepartureDate().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<FleetFlight> findByStatus(String status) {
        try {
            FlightStatus flightStatus = FlightStatus.valueOf(status.toUpperCase());
            LocalDateTime now = LocalDateTime.now();
            return flights.values().stream()
                    .filter(flight -> flight.getDepartureDate().isAfter(now) && flight.getStatus() == flightStatus)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public FleetFlight save(FleetFlight flight) {
        if (flight.getId() == null) {
            flight.setId("f" + nextId++);
        }
        flights.put(flight.getId(), flight);
        return flight;
    }
}