package com.fleet.vehicles;
import com.fleet.exceptions.*;
import com.fleet.interfaces.*;
import com.fleet.vehicles.AirVehicle;

public class Airplane extends AirVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    public Airplane(String id, String model, double maxSpeed, double maxAltitude,// airplane and it methods
                    double cargoCapacity, int passengerCapacity) throws InvalidOperationException {
        super(id, model, maxSpeed, maxAltitude);
        this.fuelLevel = 0.0;
        this.passengerCapacity = passengerCapacity;
        this.currentPassengers = 0;
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
    }


    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {// moving the airplane
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        System.out.println("Flying at " + getMaxAltitude() + " meters...");
        double fuelRequired = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelRequired) {
            throw new InsufficientFuelException("Not enough fuel to fly the airplane");
        }
        fuelLevel -= fuelRequired;
        addMileage(distance);
        if (getCurrentMileage() > 10000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {//  default setted fuel efficiency of airplane
        return 5.0; // 5 km/l
    }

    @Override
    public double estimateJourneyTime(double distance) {// estimating journey time
        return super.estimateJourneyTime(distance);
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {//refuel method of airplane
        if (amount <= 0) {
            throw new InvalidOperationException("Fuel amount must be positive");
        }
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {//get fuel level method of airplane
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {//consume fuel method of airplane
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Not enough fuel");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }

  
    @Override
    public void boardPassengers(int count) throws OverloadException {//board passengers method of airplane
        if (currentPassengers + count > passengerCapacity) {
            throw new OverloadException("Cannot board passengers, capacity exceeded");
        }
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {//disembark passengers method of airplane
        if (count > currentPassengers) {
            throw new InvalidOperationException("Cannot disembark more than current passengers");
        }
        currentPassengers -= count;
    }

    @Override
    public int getPassengerCapacity() {//get passenger capacity method of airplane
        return passengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {//get current passengers method of airplane
        return currentPassengers;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {//load cargo method of airplane
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cannot load cargo: capacity exceeded");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {//unload cargo method of airplane
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more than current cargo");
        }
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {//get cargo capacity method of airplane
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {//get current cargo method of airplane
        return currentCargo;
    }

  
    @Override
    public void scheduleMaintenance() {//schedule maintenance method of airplane
        maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {//needs maintenance method of airplane
        return maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {//perform maintenance method of airplane
        maintenanceNeeded = false;
        System.out.println("Airplane maintenance performed.");
    }
}