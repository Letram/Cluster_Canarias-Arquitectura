package com.astrobookings.infrastructure.presentation;

import com.astrobookings.domain.CancellationService;
import com.astrobookings.infrastructure.presentation.factories.RepositoryPortFactory;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AdminHandler extends BaseHandler {
    private final CancellationService cancellationService;

    public AdminHandler() {
        this.cancellationService = new CancellationService(RepositoryPortFactory.getFlightRepositoryPort(), RepositoryPortFactory.getBookingRepositoryPort());
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