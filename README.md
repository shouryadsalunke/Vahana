# Vehicle Rental System
### Core Java | OOP | File Persistence | Console Application

---

## Project Structure

```
VehicleRentalSystem/
├── src/
│   └── com/rental/
│       ├── Main.java                        ← Entry point
│       ├── model/
│       │   ├── Vehicle.java                 ← Abstract base class
│       │   ├── Car.java                     ← Concrete subclass
│       │   ├── Bike.java                    ← Concrete subclass
│       │   ├── SUV.java                     ← Concrete subclass
│       │   ├── User.java                    ← Customer model
│       │   ├── Booking.java                 ← Booking/transaction model
│       │   ├── BookingStatus.java           ← Enum (ACTIVE/CANCELLED/COMPLETED)
│       │   ├── Rentable.java                ← Interface
│       │   └── Manageable.java              ← Interface
│       ├── factory/
│       │   └── VehicleFactory.java          ← Factory Pattern
│       ├── service/
│       │   └── RentalSystem.java            ← Core business logic
│       ├── menu/
│       │   ├── UserMenu.java                ← Customer console menu
│       │   └── ManagerMenu.java             ← Admin console menu
│       └── util/
│           ├── FileHandler.java             ← Serialization I/O
│           └── InputHelper.java             ← Safe console input
├── Compile.java                             ← Bootstrap compiler
├── run.sh                                   ← Build & run script
└── README.md
```

---

## OOP Concepts Demonstrated

| Concept         | Where                                                        |
|-----------------|--------------------------------------------------------------|
| Abstraction     | `Vehicle` abstract class + `calculateRent(int days)`         |
| Encapsulation   | All fields private; getters/setters in every model class     |
| Inheritance     | `Car`, `Bike`, `SUV` all extend `Vehicle`                    |
| Polymorphism    | `calculateRent()` overridden in each subclass; dynamic dispatch in `Booking` constructor |
| Interfaces      | `Rentable` (booking contract), `Manageable` (admin contract) |
| Generics        | `ArrayList<Vehicle>`, `ArrayList<User>`, `ArrayList<Booking>`|
| Factory Pattern | `VehicleFactory.createVehicle(type, ...)`                    |
| Comparable      | `Vehicle` implements `Comparable<Vehicle>` — sort by price   |
| Enum            | `BookingStatus` — ACTIVE / CANCELLED / COMPLETED             |

---

## Pricing Logic (Polymorphism in action)

| Type | `calculateRent()` logic                        |
|------|------------------------------------------------|
| Car  | `days × price` — 5% discount if ≥ 7 days      |
| Bike | `days × price` + ₹50 surcharge for 1-day trips |
| SUV  | `days × price × 1.10` (10% luxury surcharge)   |

---

## How to Run

### Prerequisites
- Java 11+ with JDK (needs `jdk.compiler` module — included in any standard JDK)

### Option 1: Shell Script
```bash
chmod +x run.sh
./run.sh
```

### Option 2: Manual
```bash
# Compile
java --add-modules jdk.compiler Compile.java

# Run
java -cp out com.rental.Main
```

---

## Default Credentials

### Sample Users (Customer Login)
| Name          | Email              | Password  |
|---------------|--------------------|-----------|
| Alice Sharma  | alice@email.com    | alice123  |
| Bob Verma     | bob@email.com      | bob123    |
| Carol Nair    | carol@email.com    | carol123  |

### Manager
| PIN       |
|-----------|
| admin123  |

---

## Features

### User (Customer)
- Login / Register
- View all available vehicles (sorted by price)
- Search vehicles by type (CAR / BIKE / SUV)
- Filter vehicles by max price per day
- Book a vehicle (with cost preview & confirmation)
- View my bookings
- Cancel booking

### Manager (Admin)
- Add vehicle (via Factory Pattern)
- Remove vehicle (blocked if active bookings exist)
- Update vehicle fields (name / price / seats / availability)
- View all vehicles
- View all bookings
- Search bookings by user ID
- Search bookings by vehicle ID
- View all registered users

---

## File Persistence

Data is stored in a `data/` directory using Java Serialization:

| File           | Contents                      |
|----------------|-------------------------------|
| `data/vehicles.dat` | Serialized `ArrayList<Vehicle>` |
| `data/users.dat`    | Serialized `ArrayList<User>`    |
| `data/bookings.dat` | Serialized `ArrayList<Booking>` |

- Data loads on startup
- Data saves on `Save & Exit`
- Shutdown hook ensures auto-save even on abrupt exit
# Vahana
