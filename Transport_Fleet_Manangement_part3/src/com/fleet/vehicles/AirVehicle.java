package com.fleet.vehicles;

import com.fleet.exceptions.InvalidOperationException;

public abstract class AirVehicle extends Vehicle {// Air vehicles have additional properties like max altitude
    private double maxAltitude;

    public AirVehicle(String id, String model, double maxSpeed, double maxAltitude) throws InvalidOperationException {// in km/h
        super(id, model, maxSpeed);
        this.maxAltitude = maxAltitude;
    }

    public double getMaxAltitude() {// in meters
        return maxAltitude;
    }

    @Override
    public double estimateJourneyTime(double distance) {// distance in km
        double baseTime = distance / getMaxSpeed();
        return baseTime * 0.95; // 5% less time for direct paths
    }
}