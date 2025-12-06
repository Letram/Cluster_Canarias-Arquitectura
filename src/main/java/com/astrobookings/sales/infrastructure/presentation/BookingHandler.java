package com.astrobookings.sales.infrastructure.presentation;

import com.astrobookings.sales.domain.models.booking.SalesBooking;
import com.astrobookings.sales.domain.ports.input.BookingUseCases;
import com.astrobookings.sales.infrastructure.presentation.models.HTTPBooking;
import com.astrobookings.shared.infrastructure.presentation.BaseHandler;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BookingHandler extends BaseHandler {

    private final DomainMapper<SalesBooking, HTTPBooking> domainMapper;
    private final BookingUseCases bookingUseCases;

    public BookingHandler(BookingUseCases bookingUseCases, DomainMapper<SalesBooking, HTTPBooking> domainMapper) {
        this.bookingUseCases = bookingUseCases;
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
            String flightId = null;
            String passengerName = null;
            if (query != null) {
                Map<String, String> params = this.parseQuery(query);
                flightId = params.get("flightId");
                passengerName = params.get("passengerName");
            }

            List<SalesBooking> bookings = this.bookingUseCases.getBookings(flightId, passengerName);
            response = this.objectMapper.writeValueAsString(bookings.stream().map(domainMapper::toInfrastructure));
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
            JsonNode jsonNode = this.objectMapper.readTree(body);

            String flightId = jsonNode.get("flightId").asText();
            String passengerName = jsonNode.get("passengerName").asText();

            // Basic validation in handler (mixing)
            if (flightId == null || flightId.trim().isEmpty()) {
                statusCode = 400;
                response = "{\"error\": \"Flight ID must be provided\"}";
            } else if (passengerName == null || passengerName.trim().isEmpty()) {
                statusCode = 400;
                response = "{\"error\": \"Passenger name must be provided\"}";
            } else {
                response = this.objectMapper.writeValueAsString(bookingUseCases.createBooking(flightId, passengerName));
            }
        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            if (error.contains("not found")) {
                statusCode = 404;
            } else if (error.contains("not available")) {
                statusCode = 400;
            } else {
                statusCode = 400;
            }
            response = "{\"error\": \"" + error + "\"}";
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("FAILED")) {
                statusCode = 402;
                response = "{\"error\": \"Payment required\"}";
            } else {
                statusCode = 400;
                response = "{\"error\": \"Invalid JSON or request\"}";
            }
        }

        sendResponse(exchange, statusCode, response);
    }

}