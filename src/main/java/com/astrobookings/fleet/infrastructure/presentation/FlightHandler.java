package com.astrobookings.fleet.infrastructure.presentation;

import com.astrobookings.fleet.domain.models.flight.FleetFlight;
import com.astrobookings.fleet.domain.ports.input.FlightUseCases;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPFlight;
import com.astrobookings.shared.infrastructure.presentation.BaseHandler;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FlightHandler extends BaseHandler {
    private final FlightUseCases flightUseCases;
    private final DomainMapper<FleetFlight, HTTPFlight> domainMapper;

    public FlightHandler(FlightUseCases flightUseCases, DomainMapper<FleetFlight, HTTPFlight> domainMapper) {
        this.flightUseCases = flightUseCases;
        this.domainMapper = domainMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            handleGet(exchange);
        } else if ("POST".equals(method)) {
            handlePost(exchange);
        } else {
            this.handleMethodNotAllowed(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String response;
        int statusCode = 200;

        try {
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();
            String statusFilter = null;
            if (query != null) {
                Map<String, String> params = this.parseQuery(query);
                statusFilter = params.get("status");
            }

            response = this.objectMapper.writeValueAsString(flightUseCases.getFlights(statusFilter));
        } catch (Exception e) {
            statusCode = 500;
            response = "{\"error\": \"Internal server error\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String response;
        int statusCode = 201;

        try {
            // Parse JSON body
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            HTTPFlight flight = this.objectMapper.readValue(body, HTTPFlight.class);

            String error = validateFlight(flight);
            if (error != null) {
                statusCode = 400;
                response = "{\"error\": \"" + error + "\"}";
            } else {
                FleetFlight saved = flightUseCases.createFlight(domainMapper.toDomain(flight));
                response = this.objectMapper.writeValueAsString(saved);
            }

        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            if (error.contains("does not exist")) {
                statusCode = 404;
            } else {
                statusCode = 400;
            }
            response = "{\"error\": \"" + error + "\"}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            statusCode = 400;
            response = "{\"error\": \"Invalid JSON or request\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private String validateFlight(HTTPFlight flight) {
        if (flight.rocket().id() == null) {
            return "Rocket ID must be provided";
        }
        if (flight.departureDate() == null) {
            return "Departure date must be provided";
        }

        return null; // No errors
    }
}