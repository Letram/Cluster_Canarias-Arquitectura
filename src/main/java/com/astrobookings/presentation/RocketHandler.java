package com.astrobookings.presentation;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RocketHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String response = "";

    if ("GET".equals(method)) {
      // TODO: List all rockets (JSON)
      response = "[]"; // Placeholder
    } else if ("POST".equals(method)) {
      // TODO: Create rocket from JSON body
      response = "{}"; // Placeholder
    } else {
      exchange.sendResponseHeaders(405, -1); // Method not allowed
      return;
    }

    exchange.sendResponseHeaders(200, response.getBytes().length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(response.getBytes());
    }
  }
}