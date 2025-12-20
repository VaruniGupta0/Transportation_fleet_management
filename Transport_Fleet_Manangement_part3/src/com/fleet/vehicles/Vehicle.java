package com.fleet.vehicles;

import com.fleet.exceptions.InvalidOperationException;
import com.fleet.exceptions.InsufficientFuelException;

// Abstract class representing a generic vehicle in the fleet management system.Provides common properties and methods for all vehicle types.
public abstract class Vehicle implements Comparable<Vehicle> {//Comparable implementation 
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    public Vehicle(String id, String model, double maxSpeed) throws InvalidOperationException {//Constructor with validation
        if (id == null || id.isEmpty()) {
            throw new InvalidOperationException("ID cannot be empty");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public abstract void move(double distance) throws InvalidOperationException, InsufficientFuelException;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);

    public void displayInfo() {//Display vehicle information
        System.out.printf("ID: %s, Model: %s, Max Speed: %.2f km/h, Current Mileage: %.2f km%n",
                id, model, maxSpeed, currentMileage);
    }

    // getter methods
    public double getCurrentMileage() { return currentMileage; }
    public String getId() { return id; }
    public double getMaxSpeed() { return maxSpeed; }
    public String getModel() { return model; }

    protected void addMileage(double distance) {
        currentMileage += distance;
    }

    //  Comparable Implementation
 
    @Override
    public int compareTo(Vehicle other) {
        return this.id.compareToIgnoreCase(other.id);// This allows Collections.sort(fleet) to work naturally by ID.
    }
}