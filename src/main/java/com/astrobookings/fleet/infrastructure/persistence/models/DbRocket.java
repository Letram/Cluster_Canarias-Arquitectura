package com.astrobookings.fleet.infrastructure.persistence.models;

public record DbRocket(String id, String name, int capacity, double speed) {
}
