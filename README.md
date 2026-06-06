# Transportation Fleet Management & Highway Simulator

A comprehensive, three-stage Java application that simulates a dynamic logistics fleet management system. The project evolves from a strict Object-Oriented Programming (OOP) foundation into a data-driven system using the Java Collections Framework, culminating in a concurrent, multithreaded simulation engine with a Java Swing Graphical User Interface (GUI) and synchronized shared-resource allocation.



##  Evolution Paradigm

The architecture of this repository represents a multi-phase engineering pipeline:
* **Phase 1 (Core OOP Design):** Established a multi-level inheritance hierarchy across `Land`, `Air`, and `Water` vehicle domains using abstract classes, custom domain exception handling, and granular interface modeling (`FuelConsumable`, `CargoCarrier`, `PassengerCarrier`, `Maintainable`).
* **Phase 2 (Data Persistence & Architecture Optimization):** Refactored fixed-size data structures into highly optimized Java Collections (`ArrayList`, `HashSet`, `TreeSet`). Implemented strict structural uniqueness checks, custom `Comparable`/`Comparator` sorting matrices, and persistent flat-file CSV serialization streams.
* **Phase 3 (Concurrent Engine & UI Integration):** Transformed the static data model into an active runtime simulator. Assigned distinct, asynchronous lifecycles to individual vehicles via multithreading, built a reactive GUI dashboard using Swing, and intentionally isolated, monitored, and resolved a cross-thread race condition on shared highway metrics.

---

## 🛠️ Architecture Overview

The system is decoupled into a modular package layout to ensure separation of concerns:

```text
edu.univ.fleet
│
├── core                 # Abstract roots (Vehicle, LandVehicle, AirVehicle, WaterVehicle)
├── vehicles             # Concrete domain implementations (Car, Truck, Bus, Airplane, CargoShip)
├── interfaces           # Behavioral capabilities (FuelConsumable, CargoCarrier, Maintainable, etc.)
├── collections          # Custom registry layers, uniqueness checkers, and sorting comparators
├── exceptions           # Custom domain exceptions (OverloadException, InsufficientFuelException, etc.)
├── database             # Flat-file IO engine and CSV serialization/deserialization factories
├── concurrency          # Simulation Thread workers and thread-safe Mutex/ReentrantLock layers
└── gui                  # Swing Presentation layer, Event Dispatch Thread workers, and telemetry panels
