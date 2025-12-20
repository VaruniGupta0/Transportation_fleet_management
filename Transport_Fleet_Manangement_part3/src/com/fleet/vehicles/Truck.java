package com.fleet.vehicles;

import com.fleet.exceptions.*;
import com.fleet.interfaces.*;

public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable {// Truck class implementing required interfaces
    private double fuelLevel;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    public Truck(String id, String model, double maxSpeed, int numWheels,
                 double fuelLevel, double cargoCapacity) throws InvalidOperationException {// Constructor for Truck
        super(id, model, maxSpeed, numWheels);
        this.fuelLevel = fuelLevel;
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {// Overriding move method for Truck
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }

        System.out.println("Hauling cargo...");

        double efficiency = calculateFuelEfficiency();
        if (currentCargo > cargoCapacity * 0.5) {
            efficiency *= 0.9; // reduce efficiency by 10% if loaded more than 50%
        }

        double fuelRequired = distance / efficiency;
        if (fuelLevel < fuelRequired) {
            throw new InsufficientFuelException("Not enough fuel");
        }

        fuelLevel -= fuelRequired;
        addMileage(distance);

        if (getCurrentMileage() > 1000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {// Fuel efficiency calculation for Truck
        return 8.0; // 8 km/l
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {// Loading cargo method
        if (weight <= 0) {
            throw new OverloadException("Invalid cargo weight");
        }
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cannot load cargo: capacity exceeded");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {// Unloading cargo method
        if (weight <= 0 || weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more than current cargo");
        }
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {// Getting cargo capacity
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {// Getting current cargo
        return currentCargo;
    }

   
    @Override
    public void refuel(double amount) throws InvalidOperationException {// Refueling method
        if (amount <= 0) {
            throw new InvalidOperationException("Fuel amount must be positive");
        }
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {// Getting fuel level
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {// Consuming fuel based on distance
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Not enough fuel");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }

   
    @Override
    public void scheduleMaintenance() {// Scheduling maintenance
        maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {//  Checking if maintenance is needed
        return maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {// Performing maintenance
        maintenanceNeeded = false;
        System.out.println("Truck maintenance performed.");
    }
}
