package com.astrobookings.sales.infrastructure.presentation;

import com.astrobookings.sales.domain.ports.input.CancellationUseCases;
import com.astrobookings.shared.infrastructure.presentation.BaseHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AdminHandler extends BaseHandler {
    private final CancellationUseCases cancellationUseCases;

    public AdminHandler(CancellationUseCases cuc) {
        this.cancellationUseCases = cuc;
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
            response = cancellationUseCases.cancelFlights();
        } catch (Exception e) {
            statusCode = 500;
            response = "{\"error\": \"Internal server error\"}";
        }

        sendResponse(exchange, statusCode, response);
    }
}