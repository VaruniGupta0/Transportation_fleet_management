package com.fleet.vehicles;

import com.fleet.exceptions.*;
import com.fleet.interfaces.*;
import com.fleet.vehicles.LandVehicle;

public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable {//propeties and methods of car
    private double fuelLevel;
    private int PassengerCapacity;
    private int currentPassengers;
    private boolean maintenanceNeeded;

    public Car(String id, String model, double maxSpeed, int numWheels,//constructor of car
               double fuelLevel, int passengerCapacity) throws InvalidOperationException {
        super(id, model, maxSpeed, numWheels);
        this.fuelLevel = fuelLevel;
        this.PassengerCapacity = passengerCapacity;
        this.currentPassengers = 0;
        this.maintenanceNeeded = false;
    }


    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {//move method of car
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        System.out.println("Driving on road...");
        double fuelRequired = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelRequired) {
            throw new InsufficientFuelException("Not enough fuel to move the car");
        }
        fuelLevel -= fuelRequired;
        addMileage(distance);
        if (getCurrentMileage() > 1000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {//fuel efficiency method of car
        return 15.0; // 15 km/l
    }

    @Override
    public double estimateJourneyTime(double distance) {//estimate journey time method of car
        return super.estimateJourneyTime(distance);
    }

   
    @Override
    public void refuel(double amount) throws InvalidOperationException {//refuel method of car
        if (amount <= 0) {
            throw new InvalidOperationException("Fuel amount must be positive");
        }
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {//get fuel level method of car
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {//consume fuel method of car
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Not enough fuel");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }

 
    @Override
    public void boardPassengers(int count) throws OverloadException {//board passengers method of car
        if (currentPassengers + count > PassengerCapacity) {
            throw new OverloadException("Cannot board passengers: capacity exceeded");
        }
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {//disembark passengers method of car
        if (count > currentPassengers) {
            throw new InvalidOperationException("Cannot disembark more than current passengers");
        }
        currentPassengers -= count;
    }

    @Override
    public int getPassengerCapacity() {//get passenger capacity method of car
        return PassengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {//get current passengers method of car
        return currentPassengers;
    }

    
    @Override
    public void scheduleMaintenance() {//schedule maintenance method of car
        maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {//needs maintenance method of car
        return maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {//perform maintenance method of car
        maintenanceNeeded = false;
        System.out.println("Car maintenance performed.");
    }

}