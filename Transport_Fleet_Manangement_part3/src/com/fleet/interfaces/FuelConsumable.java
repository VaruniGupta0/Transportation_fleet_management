package com.fleet.interfaces;

import com.fleet.exceptions.InvalidOperationException;
import com.fleet.exceptions.InsufficientFuelException;

public interface FuelConsumable {// Interface for vehicles that consume fuel
    void refuel(double amount) throws InvalidOperationException;
    double getFuelLevel();
    double consumeFuel(double distance) throws InsufficientFuelException;
}