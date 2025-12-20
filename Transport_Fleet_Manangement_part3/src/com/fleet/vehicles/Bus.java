package com.fleet.vehicles;

import com.fleet.exceptions.*;
import com.fleet.interfaces.*;
import com.fleet.vehicles.LandVehicle;

public class Bus extends LandVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {//properties of bus
    private double fuelLevel;//private fuel level in liters
    private int passengerCapacity;
    private int currentPassengers;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    public Bus(String id, String model, double maxSpeed, int numWheels,
               double fuelLevel, int passengerCapacity, double cargoCapacity)
            throws InvalidOperationException {//constructor of bus
        super(id, model, maxSpeed, numWheels);
        this.fuelLevel = fuelLevel;
        this.passengerCapacity = passengerCapacity;
        this.currentPassengers = 0;
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
    }


    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {//move method of bus
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        System.out.println("Transporting passengers and cargo...");
        double fuelRequired = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelRequired) {
            throw new InsufficientFuelException("Not enough fuel to move the bus");
        }
        fuelLevel -= fuelRequired;
        addMileage(distance);
        if (getCurrentMileage() > 1000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return 10.0; // 10 km/l
    }

    @Override
    public double estimateJourneyTime(double distance) {
        return super.estimateJourneyTime(distance);
    }

  
    @Override
    public void refuel(double amount) throws InvalidOperationException {//refuel method of bus
        if (amount <= 0) {
            throw new InvalidOperationException("Fuel amount must be positive");
        }
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Not enough fuel");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }

   
    @Override
    public void boardPassengers(int count) throws OverloadException {//board passengers method of bus
        if (currentPassengers + count > passengerCapacity) {
            throw new OverloadException("Cannot board passengers: capacity exceeded");
        }
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {//disembark passengers method of bus
        if (count > currentPassengers) {
            throw new InvalidOperationException("Cannot disembark more than current passengers");
        }
        currentPassengers -= count;
    }

    @Override
    public int getPassengerCapacity() {//get passenger capacity method of bus
        return passengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {//get current passengers method of bus
        return currentPassengers;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {//load cargo method of bus
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cannot load cargo: capacity exceeded");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {//unload cargo method of bus
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more than current cargo");
        }
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {//get cargo capacity method of bus
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {   //get current cargo method of bus
        return currentCargo;
    }

    @Override
    public void scheduleMaintenance() {//schedule maintenance method of bus
        maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {//needs maintenance method of bus
        return maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {//perform maintenance method of bus
        maintenanceNeeded = false;
        System.out.println("Bus maintenance performed.");
    }
}