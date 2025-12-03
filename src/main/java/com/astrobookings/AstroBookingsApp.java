package com.astrobookings;

import com.astrobookings.domain.ports.input.BookingUseCases;
import com.astrobookings.domain.ports.input.CancellationUseCases;
import com.astrobookings.domain.ports.input.FlightUseCases;
import com.astrobookings.domain.ports.input.RocketUseCases;
import com.astrobookings.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.infrastructure.presentation.AdminHandler;
import com.astrobookings.infrastructure.presentation.BookingHandler;
import com.astrobookings.infrastructure.presentation.FlightHandler;
import com.astrobookings.infrastructure.presentation.RocketHandler;
import com.astrobookings.infrastructure.presentation.factories.RepositoryPortFactory;
import com.astrobookings.infrastructure.presentation.factories.UseCasesFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AstroBookingsApp {
    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        RocketRepositoryPort rocketRepository = RepositoryPortFactory.getRocketRepositoryPort();
        FlightRepositoryPort flightRepository = RepositoryPortFactory.getFlightRepositoryPort();
        BookingRepositoryPort bookingRepository = RepositoryPortFactory.getBookingRepositoryPort();

        RocketUseCases rocketUseCases = UseCasesFactory.getRocketUseCases(rocketRepository);
        FlightUseCases flightUseCases = UseCasesFactory.getFlightUseCases(flightRepository, rocketRepository);
        BookingUseCases bookingUseCases = UseCasesFactory.getBookingUseCases(bookingRepository, flightRepository, rocketRepository);
        CancellationUseCases cancellationUseCases = UseCasesFactory.getCancellationUseCases(flightRepository, bookingRepository);

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
