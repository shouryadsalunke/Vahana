package com.rental.menu;

import com.rental.model.Booking;
import com.rental.model.User;
import com.rental.model.Vehicle;
import com.rental.service.RentalSystem;
import com.rental.util.InputHelper;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles the console User (Customer) menu.
 * Covers: view vehicles, search, book, view bookings, cancel.
 */
public class UserMenu {

    private final RentalSystem system;

    public UserMenu(RentalSystem system) {
        this.system = system;
    }

    /** Entry point — shows login/register prompt, then the user menu. */
    public void show() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║        USER PORTAL           ║");
        System.out.println("╚══════════════════════════════╝");

        int choice = InputHelper.readInt(
            "  1. Login\n  2. Register\n  0. Back\n  Choice: ", 0, 2);

        User currentUser = null;
        switch (choice) {
            case 1: currentUser = handleLogin();    break;
            case 2: currentUser = handleRegister(); break;
            case 0: return;
        }

        if (currentUser == null) return; // login/register failed

        // ── User is authenticated — show menu ─────────────────────────────
        boolean running = true;
        while (running) {
            System.out.println("\n┌─────────────────────────────┐");
            System.out.printf ("│  Welcome, %-18s│%n", currentUser.getName());
            System.out.println("├─────────────────────────────┤");
            System.out.println("│  1. View Available Vehicles │");
            System.out.println("│  2. Search by Type          │");
            System.out.println("│  3. Filter by Max Price     │");
            System.out.println("│  4. Book a Vehicle          │");
            System.out.println("│  5. My Bookings             │");
            System.out.println("│  6. Cancel Booking          │");
            System.out.println("│  0. Logout                  │");
            System.out.println("└─────────────────────────────┘");

            int opt = InputHelper.readInt("  Choice: ", 0, 6);
            switch (opt) {
                case 1: viewAvailableVehicles();            break;
                case 2: searchByType();                     break;
                case 3: filterByPrice();                    break;
                case 4: bookVehicle(currentUser);           break;
                case 5: viewMyBookings(currentUser);        break;
                case 6: cancelBooking(currentUser);         break;
                case 0: running = false;                    break;
            }
        }
        System.out.println("  Logged out.");
    }

    // ─── Auth ─────────────────────────────────────────────────────────────────

    private User handleLogin() {
        System.out.println("\n  ── Login ──");
        String email = InputHelper.readString("  Email   : ");
        String pass  = InputHelper.readString("  Password: ");

        User user = system.login(email, pass);
        if (user == null) {
            System.out.println("  ✗ Invalid email or password.");
            return null;
        }
        System.out.println("  ✓ Login successful. Welcome, " + user.getName() + "!");
        return user;
    }

    private User handleRegister() {
        System.out.println("\n  ── Register ──");
        String name  = InputHelper.readString("  Full Name: ");
        String email = InputHelper.readString("  Email    : ");
        String phone = InputHelper.readString("  Phone    : ");
        String pass  = InputHelper.readString("  Password : ");

        // Generate incremental user ID
        String userId = "U" + String.format("%03d", system.getAllUsers().size() + 1);
        User newUser  = new User(userId, name, email, phone, pass);
        try {
            system.registerUser(newUser);
            System.out.println("  ✓ Registration successful! Your ID: " + userId);
            return newUser;
        } catch (Exception e) {
            System.out.println("  ✗ Registration failed: " + e.getMessage());
            return null;
        }
    }

    // ─── Vehicle Browsing ─────────────────────────────────────────────────────

    private void viewAvailableVehicles() {
        List<Vehicle> list = system.getAvailableVehicles();
        System.out.println("\n  ── Available Vehicles (sorted by price) ──");
        if (list.isEmpty()) {
            System.out.println("  No vehicles currently available.");
        } else {
            list.forEach(v -> System.out.println("  " + v));
        }
        InputHelper.pressEnterToContinue();
    }

    private void searchByType() {
        System.out.println("  Types: CAR / BIKE / SUV");
        String type = InputHelper.readString("  Enter type: ").toUpperCase();
        List<Vehicle> list = system.searchByType(type);
        System.out.println("\n  ── " + type + " results ──");
        if (list.isEmpty()) {
            System.out.println("  No available vehicles of type: " + type);
        } else {
            list.forEach(v -> System.out.println("  " + v));
        }
        InputHelper.pressEnterToContinue();
    }

    private void filterByPrice() {
        double maxPrice = InputHelper.readDouble("  Max price per day (₹): ");
        List<Vehicle> list = system.filterByMaxPrice(maxPrice);
        System.out.println("\n  ── Vehicles ≤ ₹" + maxPrice + "/day ──");
        if (list.isEmpty()) {
            System.out.println("  No vehicles found within that budget.");
        } else {
            list.forEach(v -> System.out.println("  " + v));
        }
        InputHelper.pressEnterToContinue();
    }

    // ─── Booking ─────────────────────────────────────────────────────────────

    private void bookVehicle(User user) {
        System.out.println("\n  ── Book a Vehicle ──");
        viewAvailableVehicles();

        String vehicleId = InputHelper.readString("  Enter Vehicle ID to book: ").toUpperCase();
        LocalDate today  = LocalDate.now();
        LocalDate start  = InputHelper.readDate("  Start date (yyyy-MM-dd): ", today);
        LocalDate end    = InputHelper.readDate("  End date   (yyyy-MM-dd): ", start.plusDays(1));

        try {
            // Preview cost before confirming
            Vehicle v  = system.findVehicleById(vehicleId)
                .orElseThrow(() -> new Exception("Vehicle not found."));
            long days  = java.time.temporal.ChronoUnit.DAYS.between(start, end);
            double est = v.calculateRent((int) days);
            System.out.printf("  Estimated total: ₹%.2f for %d day(s).%n", est, days);

            String confirm = InputHelper.readString("  Confirm booking? (yes/no): ");
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("  Booking cancelled.");
                return;
            }

            String bookingId = system.bookVehicle(vehicleId, user, start, end);
            System.out.println("  ✓ Booking confirmed! Booking ID: " + bookingId);
        } catch (Exception e) {
            System.out.println("  ✗ Booking failed: " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    // ─── View & Cancel ────────────────────────────────────────────────────────

    private void viewMyBookings(User user) {
        List<Booking> list = system.getBookingsByUser(user.getUserId());
        System.out.println("\n  ── My Bookings ──");
        if (list.isEmpty()) {
            System.out.println("  You have no bookings.");
        } else {
            list.forEach(b -> System.out.println("  " + b));
        }
        InputHelper.pressEnterToContinue();
    }

    private void cancelBooking(User user) {
        List<Booking> list = system.getBookingsByUser(user.getUserId());
        System.out.println("\n  ── Cancel Booking ──");
        if (list.isEmpty()) {
            System.out.println("  No bookings to cancel.");
            InputHelper.pressEnterToContinue();
            return;
        }
        list.forEach(b -> System.out.println("  " + b));

        String bookingId = InputHelper.readString("  Enter Booking ID to cancel: ").toUpperCase();

        // Ensure the booking belongs to this user
        boolean belongs = list.stream()
            .anyMatch(b -> b.getBookingId().equals(bookingId));
        if (!belongs) {
            System.out.println("  ✗ Booking not found or does not belong to you.");
            InputHelper.pressEnterToContinue();
            return;
        }

        try {
            system.cancel(bookingId);
            System.out.println("  ✓ Booking " + bookingId + " cancelled successfully.");
        } catch (Exception e) {
            System.out.println("  ✗ Cancellation failed: " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }
}
