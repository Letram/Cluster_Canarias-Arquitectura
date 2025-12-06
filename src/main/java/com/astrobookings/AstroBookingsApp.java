package com.astrobookings;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.ports.input.FlightUseCases;
import com.astrobookings.fleet.domain.ports.input.RocketUseCases;
import com.astrobookings.fleet.domain.ports.output.FlightRepositoryPort;
import com.astrobookings.fleet.domain.ports.output.RocketRepositoryPort;
import com.astrobookings.fleet.infrastructure.presentation.FlightHandler;
import com.astrobookings.fleet.infrastructure.presentation.RocketHandler;
import com.astrobookings.fleet.infrastructure.presentation.factories.FleetMapperFactory;
import com.astrobookings.fleet.infrastructure.presentation.factories.FleetRepositoryPortFactory;
import com.astrobookings.fleet.infrastructure.presentation.factories.FleetUseCasesFactory;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPFlight;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPRocket;
import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.models.flight.SalesFlight;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.domain.ports.input.CancellationUseCases;
import com.astrobookings.sales.domain.ports.output.BookingRepositoryPort;
import com.astrobookings.sales.domain.ports.output.FlightInfoProvider;
import com.astrobookings.sales.infrastructure.communication.factories.SalesFlightFactory;
import com.astrobookings.sales.infrastructure.presentation.AdminHandler;
import com.astrobookings.sales.infrastructure.presentation.BookingHandler;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesMapperFactory;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesProvidersFactory;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesRepositoryPortFactory;
import com.astrobookings.sales.infrastructure.presentation.factories.SalesUseCasesFactory;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPBooking;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPSalesFlight;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;
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

        DomainMapper<FleetRocket, HTTPRocket> rocketHandlerMapper = FleetMapperFactory.getRocketHandlerMapper();
        DomainMapper<FleetFlight, HTTPFlight> flightHandlerMapper = FleetMapperFactory.getFlightHandlerMapper(rocketRepository, rocketHandlerMapper);

        DomainMapper<SalesFlight, FleetFlight> salesFlightMapper = SalesFlightFactory.getSalesFlightMapper(bookingRepository);

        DomainMapper<SalesFlight, HTTPSalesFlight> salesFlightHandlerMapper = SalesMapperFactory.getSalesFlightHTTPMapper();
        DomainMapper<SalesBooking, HTTPBooking> bookingHTTPHandlerMapper = SalesMapperFactory.getSalesBookingHandlerMapper(salesFlightHandlerMapper);

        FlightInfoProvider fip = SalesProvidersFactory.getFlightInfoProvider(flightRepository, bookingRepository, salesFlightMapper);

        RocketUseCases rocketUseCases = FleetUseCasesFactory.getRocketUseCases(rocketRepository);
        FlightUseCases flightUseCases = FleetUseCasesFactory.getFlightUseCases(flightRepository, rocketRepository);
        BookingUseCases bookingUseCases = SalesUseCasesFactory.getBookingUseCases(bookingRepository, fip);
        CancellationUseCases cancellationUseCases = SalesUseCasesFactory.getCancellationUseCases(fip);

        // Register handlers for endpoints
        server.createContext("/rockets", new RocketHandler(rocketUseCases, rocketHandlerMapper));
        server.createContext("/flights", new FlightHandler(flightUseCases, flightHandlerMapper));
        server.createContext("/bookings", new BookingHandler(bookingUseCases, bookingHTTPHandlerMapper));
        server.createContext("/admin/cancel-flights", new AdminHandler(cancellationUseCases));

        // Start server
        server.setExecutor(null); // Use default executor
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
