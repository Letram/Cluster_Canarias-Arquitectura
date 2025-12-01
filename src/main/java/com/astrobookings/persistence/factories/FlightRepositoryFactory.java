package com.astrobookings.persistence.factories;

import com.astrobookings.persistence.InMemoryFlightRepository;
import com.astrobookings.persistence.interfaces.FlightRepository;

public class FlightRepositoryFactory {

    public static FlightRepository getFlightRepository() {
        return new InMemoryFlightRepository();
    }

}
