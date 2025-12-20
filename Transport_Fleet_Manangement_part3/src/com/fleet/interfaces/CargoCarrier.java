package com.fleet.interfaces;

import com.fleet.exceptions.OverloadException;
import com.fleet.exceptions.InvalidOperationException;

public interface CargoCarrier {// interface for vehicles that can carry cargo
    void loadCargo(double weight) throws OverloadException;  // putting weight in cargo
    void unloadCargo(double weight) throws InvalidOperationException;// unloadin cargo
    double getCargoCapacity();
    double getCurrentCargo();
}
