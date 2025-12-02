package com.astrobookings.presentation;

import com.astrobookings.domain.RocketService;
import com.astrobookings.domain.dtos.RocketDto;
import com.astrobookings.presentation.factories.PortFactory;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RocketHandler extends BaseHandler {

    private final RocketService rocketService = new RocketService(PortFactory.getRocketPort());

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

            List<RocketDto> rockets = rocketService.getAllRockets();

            response = this.objectMapper.writeValueAsString(rockets);
        } catch (Exception e) {
            statusCode = 500;
            response = "{\"error\": \"Internal server error\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        try {
            // Parse JSON body
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            RocketDto rocket = this.objectMapper.readValue(body, RocketDto.class);

            // Business validations mixed with input validation
            String error = validateRocket(rocket);
            if (error != null) {
                statusCode = 400;
                response = "{\"error\": \"" + error + "\"}";
            } else {
                RocketDto saved = rocketService.createRocket(rocket);
                statusCode = 201;
                response = this.objectMapper.writeValueAsString(saved);
            }
        } catch (Exception e) {
            statusCode = 400;
            response = "{\"error\": \"Invalid JSON or request\"}";
        }

        sendResponse(exchange, statusCode, response);
    }

    private String validateRocket(RocketDto rocket) {
        if (rocket.getName() == null || rocket.getName().trim().isEmpty()) {
            return "Rocket name must be provided";
        }
        // Speed is optional, no validation
        return null;
    }

}