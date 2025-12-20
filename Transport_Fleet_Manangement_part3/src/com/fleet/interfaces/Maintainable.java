package com.fleet.interfaces;

public interface Maintainable {//interface for objects that require maintenance
    void scheduleMaintenance();
    boolean needsMaintenance();
    void performMaintenance();
}
