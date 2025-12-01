package com.astrobookings.business;

import java.time.LocalDateTime;
import java.util.List;

import com.astrobookings.business.dtos.FlightDto;
import com.astrobookings.persistence.FlightRepository;
import com.astrobookings.persistence.RocketRepository;
import com.astrobookings.persistence.models.Flight;
import com.astrobookings.persistence.models.FlightStatus;

public class FlightService {
    private final FlightRepository flightRepository;
    private final RocketRepository rocketRepository;

    public FlightService(FlightRepository flightRepository, RocketRepository rocketRepository) {
        this.flightRepository = flightRepository;
        this.rocketRepository = rocketRepository;
    }

    public List<FlightDto> getFlights(String statusFilter) {
        if (statusFilter != null && !statusFilter.isEmpty()) {
            return flightRepository.findByStatus(statusFilter).stream().map(this::flightToDto).toList();
        } else {
            return flightRepository.findAll().stream().map(this::flightToDto).toList();
        }
    }

    public FlightDto createFlight(FlightDto flightDto) {

        // dto -> model
        Flight flight = dtoToFlight(flightDto);

        // Set defaults
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setMinPassengers(5);

        // Validate
        String error = validateFlight(flight);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Save
        Flight savedFlight = flightRepository.save(flight);
        return flightToDto(savedFlight);
    }

    private String validateFlight(Flight flight) {
        // Input structure validations

        if (flight.getBasePrice() <= 0) {
            return "Base price must be positive";
        }

        // Business validations
        if (rocketRepository.findAll().stream().noneMatch(r -> r.getId().equals(flight.getRocketId()))) {
            return "Rocket with id " + flight.getRocketId() + " does not exist";
        }

        LocalDateTime now = LocalDateTime.now();
        if (!flight.getDepartureDate().isAfter(now)) {
            return "Departure date must be in the future";
        }

        LocalDateTime oneYearAhead = now.plusYears(1);
        if (flight.getDepartureDate().isAfter(oneYearAhead)) {
            return "Departure date cannot be more than 1 year ahead";
        }

        return null;
    }

    private FlightDto flightToDto(Flight flight) {
        FlightDto flightDto = new FlightDto();
        flightDto.setId(flight.getId());
        flightDto.setRocketId(flight.getRocketId());
        flightDto.setDepartureDate(flight.getDepartureDate());
        flightDto.setBasePrice(flight.getBasePrice());
        flightDto.setMinPassengers(flight.getMinPassengers());
        flightDto.setStatus(flight.getStatus());
        return flightDto;
    }

    private Flight dtoToFlight(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setId(flightDto.getId());
        flight.setRocketId(flightDto.getRocketId());
        flight.setDepartureDate(flightDto.getDepartureDate());
        flight.setBasePrice(flightDto.getBasePrice());
        flight.setMinPassengers(flightDto.getMinPassengers());
        flight.setStatus(flightDto.getStatus());
        return flight;
    }


}