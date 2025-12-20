package com.fleet.simulator;

public class Highway { // Shared Resource
    private int totalDistance = 0;
    private boolean synchronizedMode = false; // Flag for synchronized access

    
    public void setSynchronizedMode(boolean isSafe) {//method to set synchronized mode
        this.synchronizedMode = isSafe;// set the mode
    }

    public int getTotalDistance() {//method to get total distance
        return totalDistance;
    }

    public void reset() {//method to reset total distance
        totalDistance = 0;
    }

    
    public void addDistance(int distance) {//method to add distance
        if (synchronizedMode) {// 
            
            synchronized (this) {// synchronized block to ensure thread safety so that only one thread can execute this block at a time
                int temp = totalDistance;
                try { Thread.sleep(1); } catch (InterruptedException e) {} 
                totalDistance = temp + distance;
            }
        } else {
            // Non-synchronized access (unsafe)
            int temp = totalDistance;
            try { Thread.sleep(1); } catch (InterruptedException e) {} 
            totalDistance = temp + distance;
        }
    }
}