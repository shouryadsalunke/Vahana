package com.rental.menu;

import com.rental.factory.VehicleFactory;
import com.rental.model.Booking;
import com.rental.model.Vehicle;
import com.rental.service.RentalSystem;
import com.rental.util.InputHelper;

import java.util.List;

/**
 * Handles the console Manager (Admin) menu.
 * Covers: add/remove/update vehicles, view all vehicles, view/search bookings.
 *
 * In a real system, manager credentials would be checked against a secure store.
 * Here we use a simple hardcoded PIN for demonstration.
 */
public class ManagerMenu {

    private static final String MANAGER_PIN = "admin123"; // demo PIN

    private final RentalSystem system;

    public ManagerMenu(RentalSystem system) {
        this.system = system;
    }

    public void show() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║      MANAGER PORTAL          ║");
        System.out.println("╚══════════════════════════════╝");

        // Simple PIN authentication
        String pin = InputHelper.readString("  Manager PIN: ");
        if (!pin.equals(MANAGER_PIN)) {
            System.out.println("  ✗ Access denied. Incorrect PIN.");
            return;
        }
        System.out.println("  ✓ Access granted.");

        boolean running = true;
        while (running) {
            System.out.println("\n┌──────────────────────────────┐");
            System.out.println("│       MANAGER MENU           │");
            System.out.println("├──────────────────────────────┤");
            System.out.println("│  1. Add Vehicle              │");
            System.out.println("│  2. Remove Vehicle           │");
            System.out.println("│  3. Update Vehicle           │");
            System.out.println("│  4. View All Vehicles        │");
            System.out.println("│  5. View All Bookings        │");
            System.out.println("│  6. Search Bookings by User  │");
            System.out.println("│  7. Search Bookings by Veh.  │");
            System.out.println("│  8. View All Users           │");
            System.out.println("│  0. Back                     │");
            System.out.println("└──────────────────────────────┘");

            int opt = InputHelper.readInt("  Choice: ", 0, 8);
            switch (opt) {
                case 1: addVehicle();            break;
                case 2: removeVehicle();         break;
                case 3: updateVehicle();         break;
                case 4: viewAllVehicles();       break;
                case 5: viewAllBookings();       break;
                case 6: searchByUser();          break;
                case 7: searchByVehicle();       break;
                case 8: viewAllUsers();          break;
                case 0: running = false;         break;
            }
        }
    }

    // ─── Vehicle Management ───────────────────────────────────────────────────

    private void addVehicle() {
        System.out.println("\n  ── Add Vehicle ──");
        System.out.println("  Types: CAR / BIKE / SUV");
        String type     = InputHelper.readString("  Type        : ").toUpperCase();
        String name     = InputHelper.readString("  Name        : ");
        int    seats    = InputHelper.readInt("  Seats       : ", 1, 20);
        double price    = InputHelper.readDouble("  Price/day(₹): ");

        String extra;
        switch (type) {
            case "CAR":
                extra = InputHelper.readString("  Fuel type (Petrol/Diesel/Electric): ");
                break;
            case "BIKE":
                extra = InputHelper.readString("  Engine CC (e.g. 150): ");
                break;
            case "SUV":
                extra = InputHelper.readString("  Has Sunroof? (true/false): ");
                break;
            default:
                System.out.println("  ✗ Unknown type. Vehicle not added.");
                InputHelper.pressEnterToContinue();
                return;
        }

        try {
            // Factory Pattern creates the correct subclass
            Vehicle v = VehicleFactory.createVehicle(type, name, seats, price, extra);
            system.addVehicle(v);
            System.out.println("  ✓ Vehicle added: " + v);
        } catch (Exception e) {
            System.out.println("  ✗ Failed to add vehicle: " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void removeVehicle() {
        System.out.println("\n  ── Remove Vehicle ──");
        viewAllVehicles();
        String id = InputHelper.readString("  Enter Vehicle ID to remove: ").toUpperCase();
        try {
            system.removeVehicle(id);
            System.out.println("  ✓ Vehicle " + id + " removed.");
        } catch (Exception e) {
            System.out.println("  ✗ " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private void updateVehicle() {
        System.out.println("\n  ── Update Vehicle ──");
        viewAllVehicles();
        String id    = InputHelper.readString("  Vehicle ID : ").toUpperCase();
        System.out.println("  Fields: name | price | seats | available");
        String field = InputHelper.readString("  Field name : ").toLowerCase();
        String value = InputHelper.readString("  New value  : ");
        try {
            system.updateVehicle(id, field, value);
            System.out.println("  ✓ Vehicle updated.");
            system.findVehicleById(id).ifPresent(v -> System.out.println("  " + v));
        } catch (Exception e) {
            System.out.println("  ✗ " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    // ─── View Reports ─────────────────────────────────────────────────────────

    private void viewAllVehicles() {
        List<Vehicle> list = system.getAllVehicles();
        System.out.println("\n  ── All Vehicles (" + list.size() + ") ──");
        if (list.isEmpty()) {
            System.out.println("  No vehicles in system.");
        } else {
            list.forEach(v -> System.out.println("  " + v));
        }
        InputHelper.pressEnterToContinue();
    }

    private void viewAllBookings() {
        List<Booking> list = system.getAllBookings();
        System.out.println("\n  ── All Bookings (" + list.size() + ") ──");
        if (list.isEmpty()) {
            System.out.println("  No bookings yet.");
        } else {
            list.forEach(b -> System.out.println("  " + b));
        }
        InputHelper.pressEnterToContinue();
    }

    private void searchByUser() {
        System.out.println("\n  ── Search Bookings by User ──");
        viewAllUsers();
        String userId = InputHelper.readString("  Enter User ID: ").toUpperCase();
        List<Booking> list = system.getBookingsByUser(userId);
        System.out.println("  Results (" + list.size() + "):");
        if (list.isEmpty()) {
            System.out.println("  No bookings found for user: " + userId);
        } else {
            list.forEach(b -> System.out.println("  " + b));
        }
        InputHelper.pressEnterToContinue();
    }

    private void searchByVehicle() {
        System.out.println("\n  ── Search Bookings by Vehicle ──");
        String vehicleId = InputHelper.readString("  Enter Vehicle ID: ").toUpperCase();
        List<Booking> list = system.getBookingsByVehicle(vehicleId);
        System.out.println("  Results (" + list.size() + "):");
        if (list.isEmpty()) {
            System.out.println("  No bookings found for vehicle: " + vehicleId);
        } else {
            list.forEach(b -> System.out.println("  " + b));
        }
        InputHelper.pressEnterToContinue();
    }

    private void viewAllUsers() {
        System.out.println("\n  ── Registered Users ──");
        system.getAllUsers().forEach(u -> System.out.println("  " + u));
        InputHelper.pressEnterToContinue();
    }
}
