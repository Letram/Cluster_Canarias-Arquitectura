package com.astrobookings.fleet.infrastructure.presentation;

import com.astrobookings.fleet.domain.models.rocket.FleetRocket;
import com.astrobookings.fleet.domain.ports.input.RocketUseCases;
import com.astrobookings.fleet.infrastructure.presentation.models.HTTPRocket;
import com.astrobookings.shared.infrastructure.presentation.BaseHandler;
import com.astrobookings.shared.infrastructure.presentation.mappers.DomainMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RocketHandler extends BaseHandler {

    private final RocketUseCases rocketUseCases;
    private final DomainMapper<FleetRocket, HTTPRocket> domainMapper;

    private HttpExchange exchange;

    public RocketHandler(RocketUseCases rocketUseCases, DomainMapper<FleetRocket, HTTPRocket> domainMapper) {
        this.rocketUseCases = rocketUseCases;
        this.domainMapper = domainMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        this.exchange = exchange;

        if ("GET".equals(method)) {
            handleGet();
        } else if ("POST".equals(method)) {
            handlePost();
        } else {
            this.handleMethodNotAllowed(exchange);
        }
    }

    private void handleGet() throws IOException {
        String response;
        int statusCode = 200;

        try {

            List<HTTPRocket> rockets = rocketUseCases
                    .getAllRockets()
                    .stream().map(domainMapper::toInfrastructure)
                    .toList();

            response = this.objectMapper.writeValueAsString(rockets);
        } catch (Exception e) {
            statusCode = 500;
            response = "{\"error\": \"Internal server error\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private void handlePost() throws IOException {
        String response;
        int statusCode;

        try {
            // Parse JSON body
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            HTTPRocket rocket = this.objectMapper.readValue(body, HTTPRocket.class);

            // Business validations mixed with input validation
            String error = validateRocket(rocket);
            if (error != null) {
                statusCode = 400;
                response = "{\"error\": \"" + error + "\"}";
            } else {
                FleetRocket saved = rocketUseCases.createRocket(domainMapper.toDomain(rocket));
                statusCode = 201;
                response = this.objectMapper.writeValueAsString(saved);
            }
        } catch (Exception e) {
            statusCode = 400;
            response = "{\"error\": \"Invalid JSON or request\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private String validateRocket(HTTPRocket rocket) {
        if (rocket.name() == null || rocket.name().trim().isEmpty()) {
            return "Rocket name must be provided";
        }
        // Speed is optional, no validation
        return null;
    }

}