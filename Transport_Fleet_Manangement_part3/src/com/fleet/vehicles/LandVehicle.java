package com.fleet.vehicles;

import com.fleet.exceptions.InsufficientFuelException;
import com.fleet.exceptions.InvalidOperationException;

public abstract class LandVehicle extends Vehicle {//abstract class for land vehicles
    private int numWheels;//number of wheels in pvt. variable

    public int getNumWheels() {// Getter for number of wheels
        return numWheels;
    }

    public LandVehicle(String id, String model, double maxSpeed, int numWheels) throws InvalidOperationException {// Constructor for LandVehicle
        super(id, model, maxSpeed);
        this.numWheels = numWheels;
    }

    @Override
    public double estimateJourneyTime(double distance) {// Estimating journey time considering traffic
        double baseTime = distance / getMaxSpeed();
        return baseTime * 1.1; // 10% more time for traffic
    }
    @Override
    public abstract void move(double distance) throws InvalidOperationException , InsufficientFuelException;// Abstract move method

    @Override
    public abstract double calculateFuelEfficiency();// Abstract fuel efficiency method


}