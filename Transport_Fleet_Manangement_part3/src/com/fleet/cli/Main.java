package com.fleet.cli;

import java.util.List;
import java.util.Scanner;
import com.fleet.exceptions.InvalidOperationException;
import com.fleet.interfaces.*;
import com.fleet.vehicles.*;
import com.fleet.fleet.FleetManager;

public class Main {
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
//MENU DRIVEN CLI 
        while (running) {
            System.out.println("\nTransportation Fleet Management System");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Start Journey");
            System.out.println("4. Refuel All");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. List Vehicles Needing Maintenance");
            System.out.println("11. Display Sorted Vehicles");
            System.out.println("12. Show Distinct & Ordered Models");
            System.out.println("13. Fastest / Slowest Vehicle");
            System.out.println("14. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid! please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> addVehicleMenu(manager, scanner);
                case 2 -> removeVehicleMenu(manager, scanner);
                case 3 -> startJourneyMenu(manager, scanner);
                case 4 -> refuelAllMenu(manager, scanner);
                case 5 -> {
                    manager.maintainAll();
                    System.out.println("Maintenance performed on all vehicles.");
                }
                case 6 -> System.out.println(manager.generateReport());
                case 7 -> saveFleetMenu(manager, scanner);
                case 8 -> loadFleetMenu(manager, scanner);
                case 9 -> searchByTypeMenu(manager, scanner);
                case 10 -> listMaintenanceMenu(manager);
                case 11 -> displaySortedMenu(manager, scanner);
                case 12 -> showDistinctModels(manager);
                case 13 -> showFastestAndSlowest(manager);
                case 14 -> running = false;
                default -> System.out.println("Invalid! Please enter the correct option:");
            }
        }
        scanner.close();
    }

    //THE FIRST OPTION IN CLI IS TO ADD VEHICLE 
    private static void addVehicleMenu(FleetManager manager, Scanner scanner) {
        System.out.println("Select vehicle type:");
        System.out.println("1. Car\n2. Truck\n3. Bus\n4. Airplane\n5. CargoShip");
        int typeChoice = Integer.parseInt(scanner.nextLine());
        try {
            System.out.print("Enter vehicle ID: ");//
            String id = scanner.nextLine();
            System.out.print("Enter model: ");
            String model = scanner.nextLine();
            System.out.print("Enter max speed: ");
            double maxSpeed = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter fuel level: ");
            double fuelLevel = Double.parseDouble(scanner.nextLine());
            Vehicle vehicle;

            switch (typeChoice) {
                case 1 -> {  // DETAILS FOR CAR
                    System.out.print("Enter number of wheels: ");
                    int wheels = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter passenger capacity: ");
                    int cap = Integer.parseInt(scanner.nextLine());
                    vehicle = new Car(id, model, maxSpeed, wheels, fuelLevel, cap);
                }
                case 2 -> {// DETAILS FOR TRUCK
                    System.out.print("Enter number of wheels: ");
                    int truckWheels = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter cargo capacity: ");
                    int cargo = Integer.parseInt(scanner.nextLine());
                    vehicle = new Truck(id, model, maxSpeed, truckWheels, fuelLevel, cargo);
                }
                case 3 -> {// DETAILS for bus
                    System.out.print("Enter number of wheels: ");
                    int busWheels = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter cargo capacity: ");
                    double busCargo = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter passenger capacity: ");
                    int busCap = Integer.parseInt(scanner.nextLine());
                    vehicle = new Bus(id, model, maxSpeed, busWheels, fuelLevel, busCap, busCargo);
                }
                case 4 -> {// details for airplane
                    System.out.print("Enter max altitude: ");
                    double altitude = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter cargo capacity: ");
                    double airCargo = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter passenger capacity: ");
                    int airCap = Integer.parseInt(scanner.nextLine());
                    vehicle = new Airplane(id, model, maxSpeed, altitude, airCargo, airCap);
                }
                case 5 -> {// details for cargoship
                    System.out.print("Does it have a sail? (true/false): ");
                    boolean sail = Boolean.parseBoolean(scanner.nextLine());
                    System.out.print("Enter cargo capacity: ");
                    double shipCargo = Double.parseDouble(scanner.nextLine());
                    vehicle = new CargoShip(id, model, maxSpeed, shipCargo, sail);
                }
                default -> {
                    System.out.println("Invalid type.");
                    return;
                }
            }
            manager.addVehicle(vehicle);
            System.out.println("Vehicle added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static void removeVehicleMenu(FleetManager manager, Scanner scanner) {////THE second OPTION IN CLI IS TO remove VEHICLE 
        System.out.print("Enter vehicle ID to remove: ");
        String id = scanner.nextLine();
        try {
            manager.removeVehicle(id);
            System.out.println("Vehicle removed.");//track using vehicle id
        } catch (InvalidOperationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void startJourneyMenu(FleetManager manager, Scanner scanner) {// THE third OPTION IN CLI IS TO start the journey for all VEHICLE 
        System.out.print("Enter distance for journey: ");
        double distance = Double.parseDouble(scanner.nextLine());
        manager.startAllJourneys(distance);
        System.out.println("Journey started.");
    }

    private static void refuelAllMenu(FleetManager manager, Scanner scanner) {// THE fourth OPTION IN CLI IS TO start the journey for all VEHICLE

        System.out.print("Enter fuel amount to add to all vehicles: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            int count = 0;

            
            for (Vehicle v : manager.searchByType(FuelConsumable.class)) {
                try {
                    ((FuelConsumable) v).refuel(amount);
                    count++;
                } catch (InvalidOperationException e) {
                    
                    System.out.println("Skipped " + v.getId() + ": " + e.getMessage());//error handling for -ve amount or not logic.
                }
            }
            System.out.println("Refueling complete. Total vehicles refueled: " + count);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered. Please enter a number.");
        }
    }
    private static void saveFleetMenu(FleetManager manager, Scanner scanner) {// saving fleet in csv file 
        System.out.print("Enter filename to save: ");
        String filename = scanner.nextLine();
        manager.saveToFile(filename);
        System.out.println("Fleet saved to " + filename);
    }

    private static void loadFleetMenu(FleetManager manager, Scanner scanner) {// loading fleet from csv file
        System.out.print("Enter filename to load: ");
        String filename = scanner.nextLine();
        manager.loadFromFile(filename);
        System.out.println("Fleet loaded from " + filename);
    }

    private static void searchByTypeMenu(FleetManager manager, Scanner scanner) {// searching vehicle by type
        System.out.println("Search by type:\n1.Car 2.Truck 3.Bus 4.Airplane 5.CargoShip 6.FuelConsumable 7.Maintainable");
        int typeChoice = Integer.parseInt(scanner.nextLine());
        Class<?> type;
        switch (typeChoice) {
            case 1 -> type = Car.class;
            case 2 -> type = Truck.class;
            case 3 -> type = Bus.class;
            case 4 -> type = Airplane.class;
            case 5 -> type = CargoShip.class;
            case 6 -> type = FuelConsumable.class;
            case 7 -> type = Maintainable.class;
            default -> { System.out.println("Invalid type."); return; }
        }
        List<Vehicle> result = manager.searchByType(type); // search the vehiclle by class type
        
        for (Vehicle v : result) v.displayInfo();
    }

    private static void listMaintenanceMenu(FleetManager manager) {// listing vehicle needing maintenance
        List<Vehicle> vehicles = manager.getVehiclesNeedingMaintenance();
        if (vehicles.isEmpty()) System.out.println("No vehicles need maintenance.");
        else vehicles.forEach(Vehicle::displayInfo);
    }


    private static void displaySortedMenu(FleetManager manager, Scanner scanner) {//displaySorted Menu
        System.out.println("Sort vehicles by:");
        System.out.println("1. Model (A–Z)\n2. Max Speed (High–Low)\n3. Fuel Efficiency (High–Low)");
        int opt = Integer.parseInt(scanner.nextLine());
        List<Vehicle> sorted;// sorted list of vehicle
        switch (opt) {
            case 1 -> sorted = manager.getFleetSortedByModel();
            case 2 -> sorted = manager.getFleetSortedByMaxSpeed(true);
            case 3 -> sorted = manager.getFleetSortedByEfficiency(true);
            default -> { System.out.println("Invalid choice."); return; }
        }
        sorted.forEach(Vehicle::displayInfo);
    }

    private static void showDistinctModels(FleetManager manager) {// distinct and ordered model names
        System.out.println("Distinct model names (ordered):");
        for (String model : manager.getOrderedModels())
            System.out.println("• " + model);
        System.out.println("Total distinct models: " + manager.getOrderedModels().size());
    }

    private static void showFastestAndSlowest(FleetManager manager) {// fastest and slowest vehicle
        manager.getFastestVehicle().ifPresentOrElse(
                v -> { System.out.println("Fastest vehicle:"); v.displayInfo(); },
                () -> System.out.println("No vehicles found.")
        );
        manager.getSlowestVehicle().ifPresentOrElse(
                v -> { System.out.println("Slowest vehicle:"); v.displayInfo(); },
                () -> System.out.println("No vehicles found.")
        );
    }
}

