package com.fleet.interfaces;

import com.fleet.exceptions.OverloadException;
import com.fleet.exceptions.InvalidOperationException;

public interface PassengerCarrier {// Interface for vehicles that can carry passengers
    void boardPassengers(int count) throws OverloadException;
    void disembarkPassengers(int count) throws InvalidOperationException;
    int getPassengerCapacity();
    int getCurrentPassengers();
}
