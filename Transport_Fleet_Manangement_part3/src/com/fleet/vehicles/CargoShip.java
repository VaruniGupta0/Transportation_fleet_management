package com.fleet.vehicles;
import com.fleet.exceptions.*;
import com.fleet.interfaces.*;
import com.fleet.vehicles.WaterVehicle;
public class CargoShip extends WaterVehicle implements CargoCarrier, Maintainable, FuelConsumable {// CargoShip can be sail-powered or fueled
    private double cargoCapacity;//mehtods and properties for Cargoship
    private double currentCargo;
    private boolean maintenanceNeeded;
    private double fuelLevel;
    private boolean isFueled;

    public CargoShip(String id, String model, double maxSpeed, double cargoCapacity,
                     boolean hasSail) throws InvalidOperationException {// hasSail indicates if the ship is sail-powered
        super(id, model, maxSpeed, hasSail);
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
        this.isFueled = !hasSail;
        this.fuelLevel = isFueled ? 0.0 : -1;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {// Overriding move method for CargoShip
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        System.out.println("Sailing with cargo...");
        if (isFueled) {
            double fuelRequired = distance / calculateFuelEfficiency();
            if (fuelLevel < fuelRequired) {
                throw new InsufficientFuelException("Not enough fuel to move the cargo ship");
            }
            fuelLevel -= fuelRequired;
        }
        addMileage(distance);
        if (getCurrentMileage() > 10000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {// Fuel efficiency depends on whether the ship is fueled or sail-powered
        if (isFueled) {
            return 4.0; // 4 km/l
        } else {
            return 0; // No fuel consumption for sail-powered ships
        }
    }
    
    @Override
    public void loadCargo(double weight) throws OverloadException {// Loading cargo method
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cannot load cargo: capacity exceeded");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {// Unloading cargo method
        if (weight > currentCargo) {
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
    public void scheduleMaintenance() {// Scheduling maintenance
        maintenanceNeeded = true;
    }
    @Override
    public boolean needsMaintenance() {// Checking if maintenance is needed
        return maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {// Performing maintenance
        maintenanceNeeded = false;
        System.out.println("Cargo ship maintenance performed.");
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {// Refueling method
        if (!isFueled) {
            throw new InvalidOperationException("This ship does not require fuel");
        }
        if (amount <= 0) {
            throw new InvalidOperationException("Fuel amount must be positive");
        }
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {// Getting fuel level
        return isFueled ? fuelLevel : 0;
    }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {// Consuming fuel based on distance
        if (!isFueled) {
            return 0;
        }
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Not enough fuel");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }
}