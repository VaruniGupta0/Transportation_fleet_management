package com.fleet.fleet;

import java.util.*;
import java.io.*;
import com.fleet.exceptions.*;
import com.fleet.interfaces.*;
import com.fleet.vehicles.*;


public class FleetManager {

    //data strucures use in collections
    private List<Vehicle> fleet;             
    private Set<String> vehicleIds;           
    private SortedSet<String> orderedModels;  
    // Constructor
    public FleetManager() {
        fleet = new ArrayList<>();// ArrayList for dynamic sizing
        vehicleIds = new HashSet<>();// HashSet for fast ID lookup
        orderedModels = new TreeSet<>();// TreeSet for ordered model names
    }

    // add/remove vehicles
    public void addVehicle(Vehicle v) throws InvalidOperationException {//check for duplicate vehicle IDs
        if (v == null || v.getId() == null || v.getId().isEmpty()) {//validate vehicle and ID
            throw new InvalidOperationException("Invalid vehicle or ID");
        }
        if (!vehicleIds.add(v.getId())) {//check for duplicate IDs through HashSet
            throw new InvalidOperationException("Duplicate vehicle ID: " + v.getId());
        }
        fleet.add(v);//add to fleet- the ArrayList
        orderedModels.add(v.getModel());//add model name to ordered set A treeSet so it is ordered
    }

    public void removeVehicle(String id) throws InvalidOperationException {//remove vehicle by ID
        Iterator<Vehicle> iterator = fleet.iterator();//using iterator to safely remove while iterating
        while (iterator.hasNext()) {//iterate through fleet
            Vehicle v = iterator.next();
            if (v.getId().equals(id)) {
                iterator.remove();
                vehicleIds.remove(id);

                // remove model name if no other vehicle has it
                boolean sameModelExists = fleet.stream()
                        .anyMatch(x -> x.getModel().equals(v.getModel()));
                if (!sameModelExists) orderedModels.remove(v.getModel());
                return;
            }
        }
        throw new InvalidOperationException("Vehicle not found: " + id);
    }

    public void startAllJourneys(double distance) {//start journey for all vehicles
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
            } catch (InvalidOperationException | InsufficientFuelException e) {
                System.out.println("Error moving vehicle " + v.getId() + ": " + e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance) {// method to calculate total fuel consumption for a given distance
        double fuel = 0.0;
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                try {
                    fuel += ((FuelConsumable) v).consumeFuel(distance);
                } catch (InsufficientFuelException e) {
                    System.out.println("Insufficient fuel for vehicle " + v.getId() + ": " + e.getMessage());
                }
            }
        }
        return fuel;
    }

    public void maintainAll() {//method to perform maintenance on all vehicles that need it
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) {
                    m.performMaintenance();
                }
            }
        }
    }

  
    public List<Vehicle> searchByType(Class<?> type) {//method to search vehicles by its class type
        List<Vehicle> result = new ArrayList<>();// list to store results
        for (Vehicle v : fleet) {
            if (type.isInstance(v)) {
                result.add(v);
            }
        }
        return result;
    }

    public void sortFleetByEfficiency() {//method to sort fleet by fuel efficiency
        Collections.sort(fleet, new Comparator<Vehicle>() {// comparator class
            @Override
            public int compare(Vehicle v1, Vehicle v2) {
                return Double.compare(v1.calculateFuelEfficiency(), v2.calculateFuelEfficiency());
            }
        });
    }

    public String generateReport() {//method to generate a report of the fleet
        StringBuilder report = new StringBuilder();
        report.append("Fleet Report:\n");
        report.append("Total vehicles: ").append(fleet.size()).append("\n");

        
        int cars = 0, trucks = 0, buses = 0, airplanes = 0, ships = 0;
        for (Vehicle v : fleet) {
            if (v instanceof Car) cars++;
            else if (v instanceof Truck) trucks++;
            else if (v instanceof Bus) buses++;
            else if (v instanceof Airplane) airplanes++;
            else if (v instanceof CargoShip) ships++;
        }
        report.append("Cars: ").append(cars).append("\n");
        report.append("Trucks: ").append(trucks).append("\n");
        report.append("Buses: ").append(buses).append("\n");
        report.append("Airplanes: ").append(airplanes).append("\n");
        report.append("CargoShips: ").append(ships).append("\n");

        double totalMileage = 0;
        double totalEfficiency = 0;
        int countEfficiency = 0;
        for (Vehicle v : fleet) {
            totalMileage += v.getCurrentMileage();
            totalEfficiency += v.calculateFuelEfficiency();
            countEfficiency++;
        }
        double avgEfficiency = countEfficiency > 0 ? totalEfficiency / countEfficiency : 0;
        report.append("Total mileage: ").append(totalMileage).append(" km\n");
        report.append("Average fuel efficiency: ").append(avgEfficiency).append(" km/l\n");

        int needMaintenance = 0;
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                if (((Maintainable) v).needsMaintenance()) {
                    needMaintenance++;
                }
            }
        }
        report.append("Vehicles needing maintenance: ").append(needMaintenance).append("\n");

        return report.toString();
    }

    public List<Vehicle> getVehiclesNeedingMaintenance() {//method to get list of vehicles needing maintenance
        List<Vehicle> result = new ArrayList<>();// list to store results
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                if (((Maintainable) v).needsMaintenance()) {
                    result.add(v);
                }
            }
        }
        return result;
    }


    public void saveToFile(String filename) {//method to save fleet data to a file
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle veh : fleet) {
                String type = "";
                String data = "";

                if (veh instanceof Car) {
                    type = "Car";
                    Car car = (Car) veh;
                    data = String.format("%s,%s,%.2f,%d,%.2f,%d,%d",
                            car.getId(), car.getModel(), car.getMaxSpeed(),
                            car.getNumWheels(), car.getFuelLevel(),
                            car.getPassengerCapacity(), car.getCurrentPassengers());
                } else if (veh instanceof Truck) {
                    type = "Truck";
                    Truck truck = (Truck) veh;
                    data = String.format("%s,%s,%.2f,%d,%.2f,%.2f,%.2f",
                            truck.getId(), truck.getModel(), truck.getMaxSpeed(),
                            truck.getNumWheels(), truck.getFuelLevel(),
                            truck.getCargoCapacity(), truck.getCurrentCargo());
                } else if (veh instanceof Bus) {
                    type = "Bus";
                    Bus bus = (Bus) veh;
                    data = String.format("%s,%s,%.2f,%d,%.2f,%.2f,%.2f,%d,%d",
                            bus.getId(), bus.getModel(), bus.getMaxSpeed(),
                            bus.getNumWheels(), bus.getFuelLevel(),
                            bus.getCargoCapacity(), bus.getCurrentCargo(),
                            bus.getPassengerCapacity(), bus.getCurrentPassengers());
                } else if (veh instanceof Airplane) {
                    type = "Airplane";
                    Airplane airplane = (Airplane) veh;
                    data = String.format("%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%d,%d",
                            airplane.getId(), airplane.getModel(), airplane.getMaxSpeed(),
                            airplane.getMaxAltitude(), airplane.getFuelLevel(),
                            airplane.getCargoCapacity(), airplane.getCurrentCargo(),
                            airplane.getPassengerCapacity(), airplane.getCurrentPassengers());
                } else if (veh instanceof CargoShip) {
                    type = "CargoShip";
                    CargoShip cargoShip = (CargoShip) veh;
                    data = String.format("%s,%s,%.2f,%.2f,%.2f,%.2f,%b",
                            cargoShip.getId(), cargoShip.getModel(), cargoShip.getMaxSpeed(),
                            cargoShip.getFuelLevel(), cargoShip.getCargoCapacity(),
                            cargoShip.getCurrentCargo(), cargoShip.hasSail());
                }

                writer.println(type + "," + data);
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {//method to load fleet data from a file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                Vehicle vehicle = null;

                switch (type) {
                    case "Car": {
                        String id = parts[1];
                        String model = parts[2];
                        double maxSpeed = Double.parseDouble(parts[3]);
                        int numWheels = Integer.parseInt(parts[4]);
                        double fuelLevel = Double.parseDouble(parts[5]);
                        int passengerCapacity = Integer.parseInt(parts[6]);
                        int currentPassengers = Integer.parseInt(parts[7]);
                        vehicle = new Car(id, model, maxSpeed, numWheels, fuelLevel, passengerCapacity);
                        ((Car) vehicle).refuel(fuelLevel);
                        ((Car) vehicle).boardPassengers(currentPassengers);
                        break;
                    }
                    case "Truck": {
                        String id = parts[1];
                        String model = parts[2];
                        double maxSpeed = Double.parseDouble(parts[3]);
                        int numWheels = Integer.parseInt(parts[4]);
                        double fuelLevel = Double.parseDouble(parts[5]);
                        double cargoCapacity = Double.parseDouble(parts[6]);
                        double currentCargo = Double.parseDouble(parts[7]);
                        vehicle = new Truck(id, model, maxSpeed, numWheels, fuelLevel, cargoCapacity);
                        ((Truck) vehicle).refuel(fuelLevel);
                        ((Truck) vehicle).loadCargo(currentCargo);
                        break;
                    }
                    case "Bus": {
                        String id = parts[1];
                        String model = parts[2];
                        double maxSpeed = Double.parseDouble(parts[3]);
                        int numWheels = Integer.parseInt(parts[4]);
                        double fuelLevel = Double.parseDouble(parts[5]);
                        double cargoCapacity = Double.parseDouble(parts[6]);
                        double currentCargo = Double.parseDouble(parts[7]);
                        int passengerCapacity = Integer.parseInt(parts[8]);
                        int currentPassengers = Integer.parseInt(parts[9]);
                        vehicle = new Bus(id, model, maxSpeed, numWheels, fuelLevel, passengerCapacity, cargoCapacity);
                        ((Bus) vehicle).refuel(fuelLevel);
                        ((Bus) vehicle).loadCargo(currentCargo);
                        ((Bus) vehicle).boardPassengers(currentPassengers);
                        break;
                    }
                    case "Airplane": {
                        String id = parts[1];
                        String model = parts[2];
                        double maxSpeed = Double.parseDouble(parts[3]);
                        double maxAltitude = Double.parseDouble(parts[4]);
                        double fuelLevel = Double.parseDouble(parts[5]);
                        double cargoCapacity = Double.parseDouble(parts[6]);
                        double currentCargo = Double.parseDouble(parts[7]);
                        int passengerCapacity = Integer.parseInt(parts[8]);
                        int currentPassengers = Integer.parseInt(parts[9]);
                        vehicle = new Airplane(id, model, maxSpeed, maxAltitude, cargoCapacity, passengerCapacity);
                        ((Airplane) vehicle).refuel(fuelLevel);
                        ((Airplane) vehicle).loadCargo(currentCargo);
                        ((Airplane) vehicle).boardPassengers(currentPassengers);
                        break;
                    }
                    case "CargoShip": {
                        String id = parts[1];
                        String model = parts[2];
                        double maxSpeed = Double.parseDouble(parts[3]);
                        double fuelLevel = Double.parseDouble(parts[4]);
                        double cargoCapacity = Double.parseDouble(parts[5]);
                        double currentCargo = Double.parseDouble(parts[6]);
                        boolean hasSail = Boolean.parseBoolean(parts[7]);
                        vehicle = new CargoShip(id, model, maxSpeed, cargoCapacity, hasSail);
                        ((CargoShip) vehicle).refuel(fuelLevel);
                        ((CargoShip) vehicle).loadCargo(currentCargo);
                        break;
                    }
                    default:
                        System.out.println("Unknown vehicle type: " + type);
                }
                if (vehicle != null) {
                    fleet.add(vehicle);
                    vehicleIds.add(vehicle.getId());
                    orderedModels.add(vehicle.getModel());
                }
            }
        } catch (IOException | InvalidOperationException | OverloadException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }

    

    public SortedSet<String> getOrderedModels() {//get ordered set of model names using TreeSet for ordering and unmodifiable to prevent external changes.
        return Collections.unmodifiableSortedSet(orderedModels);
    }

    public List<Vehicle> getFleetSortedByModel() {//get fleet sorted by model name using ArrayList and Comparator 
        List<Vehicle> sorted = new ArrayList<>(fleet);
        sorted.sort(Comparator.comparing(Vehicle::getModel));
        return sorted;
    }

    public List<Vehicle> getFleetSortedByMaxSpeed(boolean descending) {//get fleet sorted by max speed 
        List<Vehicle> sorted = new ArrayList<>(fleet);
        Comparator<Vehicle> cmp = Comparator.comparingDouble(Vehicle::getMaxSpeed);
        if (descending) cmp = cmp.reversed();
        sorted.sort(cmp);
        return sorted;
    }

    public List<Vehicle> getFleetSortedByEfficiency(boolean descending) {//get fleet sorted by fuel efficiency
        List<Vehicle> sorted = new ArrayList<>(fleet);
        Comparator<Vehicle> cmp = Comparator.comparingDouble(Vehicle::calculateFuelEfficiency);
        if (descending) cmp = cmp.reversed();
        sorted.sort(cmp);
        return sorted;
    }

    public Optional<Vehicle> getFastestVehicle() {//get vehicle with highest max speed
        return fleet.stream().max(Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }

    public Optional<Vehicle> getSlowestVehicle() {//get vehicle with lowest max speed
        return fleet.stream().min(Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }
}




