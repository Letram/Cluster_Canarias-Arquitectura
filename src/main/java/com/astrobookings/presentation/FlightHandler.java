package com.astrobookings.presentation;

import com.astrobookings.domain.FlightService;
import com.astrobookings.domain.dtos.FlightDto;
import com.astrobookings.presentation.factories.PortFactory;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FlightHandler extends BaseHandler {
    private final FlightService flightService;

    public FlightHandler() {
        this.flightService = new FlightService(PortFactory.getFlightPort(), PortFactory.getRocketPort());
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

            response = this.objectMapper.writeValueAsString(flightService.getFlights(statusFilter));
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
            FlightDto flight = this.objectMapper.readValue(body, FlightDto.class);

            String error = validateFlight(flight);
            if (error != null) {
                statusCode = 400;
                response = "{\"error\": \"" + error + "\"}";
            } else {
                FlightDto saved = flightService.createFlight(flight);
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
            statusCode = 400;
            response = "{\"error\": \"Invalid JSON or request\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private String validateFlight(FlightDto flight) {
        if (flight.getRocketId() == null || flight.getRocketId().trim().isEmpty()) {
            return "Rocket ID must be provided";
        }
        if (flight.getDepartureDate() == null) {
            return "Departure date must be provided";
        }

        return null; // No errors
    }
}