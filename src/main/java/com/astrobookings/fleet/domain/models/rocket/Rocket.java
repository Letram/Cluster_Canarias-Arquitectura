package com.astrobookings.fleet.domain.models.rocket;

public class Rocket {
    private String id;
    private String name;
    private RocketCapacity capacity;
    private RocketSpeed speed;

    public Rocket() {
    }

    public Rocket(String id, String name, RocketCapacity capacity, RocketSpeed speed) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity.value();
    }

    public void setCapacity(RocketCapacity capacity) {
        this.capacity = capacity;
    }

    public Double getSpeed() {
        return speed.value();
    }

    public void setSpeed(RocketSpeed speed) {
        this.speed = speed;
    }
}