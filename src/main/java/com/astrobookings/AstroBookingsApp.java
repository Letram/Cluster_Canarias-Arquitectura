package com.astrobookings;

import com.astrobookings.fleet.domain.ports.input.FlightUseCases;
import com.astrobookings.fleet.domain.ports.input.RocketUseCases;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.fleet.infrastructure.presentation.FlightHandler;
import com.astrobookings.fleet.infrastructure.presentation.RocketHandler;
import com.astrobookings.fleet.infrastructure.presentation.factories.FleetRepositoryPortFactory;
import com.astrobookings.fleet.infrastructure.presentation.factories.FleetUseCasesFactory;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.domain.ports.input.CancellationUseCases;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.infrastructure.presentation.AdminHandler;
import com.astrobookings.sales.infrastructure.presentation.BookingHandler;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesProvidersFactory;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesRepositoryPortFactory;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesUseCasesFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AstroBookingsApp {
    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        RocketRepositoryPort rocketRepository = FleetRepositoryPortFactory.getRocketRepositoryPort();
        FlightRepositoryPort flightRepository = FleetRepositoryPortFactory.getFlightRepositoryPort();
        BookingRepositoryPort bookingRepository = SalesRepositoryPortFactory.getBookingRepositoryPort();

        FlightInfoProvider fip = SalesProvidersFactory.getFlightInfoProvider(flightRepository, rocketRepository);

        RocketUseCases rocketUseCases = FleetUseCasesFactory.getRocketUseCases(rocketRepository);
        FlightUseCases flightUseCases = FleetUseCasesFactory.getFlightUseCases(flightRepository, rocketRepository);
        BookingUseCases bookingUseCases = SalesUseCasesFactory.getBookingUseCases(bookingRepository, fip);
        CancellationUseCases cancellationUseCases = SalesUseCasesFactory.getCancellationUseCases(flightRepository, bookingRepository);

        // Register handlers for endpoints
        server.createContext("/rockets", new RocketHandler(rocketUseCases));
        server.createContext("/flights", new FlightHandler(flightUseCases));
        server.createContext("/bookings", new BookingHandler(bookingUseCases));
        server.createContext("/admin/cancel-flights", new AdminHandler(cancellationUseCases));

        // Start server
        server.setExecutor(null); // Use default executor
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
