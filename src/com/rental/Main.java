package com.rental;

import com.rental.menu.ManagerMenu;
import com.rental.menu.UserMenu;
import com.rental.service.RentalSystem;
import com.rental.util.InputHelper;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║          VEHICLE RENTAL SYSTEM — Main Entry Point        ║
 * ║          Core Java | OOP | File Persistence              ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * Run with: java -cp out com.rental.Main
 *
 * OOP concepts demonstrated:
 *   Abstraction    → Vehicle (abstract class + abstract calculateRent)
 *   Encapsulation  → All model fields are private with getters/setters
 *   Inheritance    → Car, Bike, SUV extend Vehicle
 *   Polymorphism   → calculateRent() overridden in each subclass
 *   Interface      → Rentable, Manageable
 *   Generics       → ArrayList<Vehicle>, ArrayList<Booking>, etc.
 *   Factory Pattern→ VehicleFactory.createVehicle(type, ...)
 */
public class Main {

    public static void main(String[] args) {

        printBanner();

        // ── Bootstrap the service layer ──────────────────────────────────────
        RentalSystem system = RentalSystem.getInstance();

        // Load persisted data (vehicles.dat, users.dat, bookings.dat)
        System.out.println("\n  Loading data...");
        system.loadData();

        // If first run, populate sample data so the system isn't empty
        system.seedSampleData();

        // Add shutdown hook — saves data even if user closes the terminal
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n  [Auto-save on exit]");
            system.saveData();
        }));

        // ── Build menus ──────────────────────────────────────────────────────
        UserMenu    userMenu    = new UserMenu(system);
        ManagerMenu managerMenu = new ManagerMenu(system);

        // ── Main loop ────────────────────────────────────────────────────────
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║    VEHICLE RENTAL SYSTEM     ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. User (Customer) Portal   ║");
            System.out.println("║  2. Manager (Admin) Portal   ║");
            System.out.println("║  3. Save & Exit              ║");
            System.out.println("╚══════════════════════════════╝");

            int choice = InputHelper.readInt("  Choice: ", 1, 3);

            switch (choice) {
                case 1:
                    userMenu.show();
                    break;
                case 2:
                    managerMenu.show();
                    break;
                case 3:
                    System.out.println("\n  Saving data...");
                    system.saveData();
                    System.out.println("  Goodbye! 🚗");
                    running = false;
                    break;
            }
        }

        System.exit(0);
    }

    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔═══════════════════════════════════════════╗");
        System.out.println("  ║       VEHICLE RENTAL SYSTEM  v1.0         ║");
        System.out.println("  ║   Core Java | OOP | File Serialization    ║");
        System.out.println("  ╚═══════════════════════════════════════════╝");
    }
}
