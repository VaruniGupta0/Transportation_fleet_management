package com.fleet.vehicles;
import com.fleet.exceptions.InvalidOperationException;

public abstract class WaterVehicle extends Vehicle {// WaterVehicle abstract class
    private boolean hasSail;

    public WaterVehicle(String id, String model, double maxSpeed, boolean hasSail) throws InvalidOperationException {// Constructor
        super(id, model, maxSpeed);
        this.hasSail = hasSail;
    }

    public boolean hasSail() {// Checking if the vehicle has a sail
        return hasSail;
    }


    @Override
    public double estimateJourneyTime(double distance) {// Estimating journey time for water vehicles
        double baseTime = distance / getMaxSpeed();
        return baseTime * 1.15; // 15% more time for currents
    }


}