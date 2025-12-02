package com.astrobookings.presentation;

import com.astrobookings.domain.CancellationService;
import com.astrobookings.presentation.factories.PortFactory;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AdminHandler extends BaseHandler {
    private final CancellationService cancellationService;

    public AdminHandler() {
        this.cancellationService = new CancellationService(PortFactory.getFlightPort(), PortFactory.getBookingPort());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("POST".equals(method)) {
            handlePost(exchange);
        } else {
            this.handleMethodNotAllowed(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        try {
            response = cancellationService.cancelFlights();
        } catch (Exception e) {
            statusCode = 500;
            response = "{\"error\": \"Internal server error\"}";
        }

        sendResponse(exchange, statusCode, response);
    }
}