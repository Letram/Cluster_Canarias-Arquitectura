package com.astrobookings.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.astrobookings.persistence.RocketRepository;
import com.astrobookings.persistence.models.Rocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RocketHandler implements HttpHandler {
  private final RocketRepository rocketRepository = new RocketRepository();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String response = "";
    int statusCode = 200;

    if ("GET".equals(method)) {
      try {
        response = objectMapper.writeValueAsString(rocketRepository.findAll());
      } catch (Exception e) {
        statusCode = 500;
        response = "{\"error\": \"Internal server error\"}";
      }
    } else if ("POST".equals(method)) {
      try {
        // Parse JSON body
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Rocket rocket = objectMapper.readValue(body, Rocket.class);

        // Business validations mixed with input validation
        String error = validateRocket(rocket);
        if (error != null) {
          statusCode = 400;
          response = "{\"error\": \"" + error + "\"}";
        } else {
          Rocket saved = rocketRepository.save(rocket);
          statusCode = 201;
          response = objectMapper.writeValueAsString(saved);
        }
      } catch (Exception e) {
        statusCode = 400;
        response = "{\"error\": \"Invalid JSON or request\"}";
      }
    } else {
      statusCode = 405;
      response = "{\"error\": \"Method not allowed\"}";
      exchange.sendResponseHeaders(statusCode, response.getBytes().length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes());
      }
      return;
    }

    exchange.sendResponseHeaders(statusCode, response.getBytes().length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(response.getBytes());
    }
  }

  private String validateRocket(Rocket rocket) {
    if (rocket.getName() == null || rocket.getName().trim().isEmpty()) {
      return "Rocket name must be provided";
    }
    if (rocket.getCapacity() <= 0 || rocket.getCapacity() > 10) {
      return "Rocket capacity must be between 1 and 10";
    }
    // Speed is optional, no validation
    return null;
  }
}