package com.astrobookings.presentation;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AdminHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String response = "";

    if ("POST".equals(method)) {
      // TODO: Trigger flight cancellation check
      response = "Cancellations processed"; // Placeholder
    } else {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    exchange.sendResponseHeaders(200, response.getBytes().length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(response.getBytes());
    }
  }
}