package com.fleet.simulator;

import com.fleet.interfaces.FuelConsumable;
import com.fleet.vehicles.Vehicle;
import javax.swing.*;

public class VehicleThread implements Runnable {// Each vehicle will run  in its own thread
    private Vehicle vehicle;
    private Highway highway;
    private JLabel statusLabel;
    private JProgressBar fuelBar;
    private JLabel mileageLabel;

    private volatile boolean running = false;
    private volatile boolean paused = false;

    public VehicleThread(Vehicle vehicle, Highway highway, JLabel statusLabel, JProgressBar fuelBar, JLabel mileageLabel) {// Dependency Injection of vehicle and highway
        this.vehicle = vehicle;
        this.highway = highway;
        this.statusLabel = statusLabel;
        this.fuelBar = fuelBar;
        this.mileageLabel = mileageLabel;
    }

    public void startSimulation() {//method to start the thread
        running = true;
        paused = false;
        new Thread(this).start();
    }

    public void stopSimulation() {//method to stop the thread
        running = false;
    }

    public void setPaused(boolean isPaused) {//method to pause/resume the thread
        this.paused = isPaused;
        updateStatus(isPaused ? "Paused" : "Running");
    }

    
    public void refuel() {//method to refuel the vehicle
        if (vehicle instanceof FuelConsumable) {
            try {
                ((FuelConsumable) vehicle).refuel(50); // Add 50 liters
                updateStatus("Refueled & Running");
                paused = false; // Auto-resume on refuel
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void run() {// Main simulation loop
        updateStatus("Running");

        while (running) {
            try {
                if (paused) {
                    Thread.sleep(100);
                    continue;
                }

                
                if (vehicle instanceof FuelConsumable) {// Fuel check bbefore moving
                    double currentFuel = ((FuelConsumable) vehicle).getFuelLevel();
                    if (currentFuel <= 0) {
                        updateStatus("OUT OF FUEL");
                        paused = true;
                        continue;
                    }
                   
                    SwingUtilities.invokeLater(() -> fuelBar.setValue((int) currentFuel)); // Update fuel bar
                }

                
                int distanceStep = 10;// Move 10 km per iteration
                vehicle.move(distanceStep);// Update vehicle state

               
                highway.addDistance(distanceStep);

                
                SwingUtilities.invokeLater(() -> {// Update mileage label
                    mileageLabel.setText(String.format("%.2f km", vehicle.getCurrentMileage()));
                });

                Thread.sleep(1000);//1 second delay

            } catch (Exception e) {
                // Handle insufficient fuel exception from move() logic
                if (e.getClass().getSimpleName().contains("InsufficientFuel")) {
                    updateStatus("OUT OF FUEL");
                    paused = true;
                }
            }
        }
        updateStatus("Stopped");
    }

   
    private void updateStatus(String text) {//method to update status label in GUI safely
        SwingUtilities.invokeLater(() -> statusLabel.setText(text));
    }
}