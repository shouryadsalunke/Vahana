package com.rental.service;

import com.rental.model.*;
import com.rental.util.FileHandler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central service class that implements Rentable and Manageable.
 * Holds all in-memory data and orchestrates every business operation.
 *
 * Implements: Rentable (booking logic), Manageable (admin operations).
 * Uses: ArrayList<Vehicle>, ArrayList<User>, ArrayList<Booking> — Generics.
 */
public class RentalSystem implements Rentable, Manageable {

    // ─── In-memory data stores (Generics) ────────────────────────────────────
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<User>    users    = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    // Booking ID counter
    private int bookingCounter = 1;

    // ─── Singleton (optional pattern, keeps one service instance) ────────────
    private static RentalSystem instance;
    public static RentalSystem getInstance() {
        if (instance == null) instance = new RentalSystem();
        return instance;
    }

    // ─── Data Persistence ────────────────────────────────────────────────────

    /** Load all data from .dat files at startup. */
    public void loadData() {
        vehicles = FileHandler.loadVehicles();
        users    = FileHandler.loadUsers();
        bookings = FileHandler.loadBookings();

        // Re-sync booking counter
        bookings.stream()
            .map(b -> b.getBookingId().replaceAll("[^0-9]", ""))
            .filter(s -> !s.isEmpty())
            .mapToInt(Integer::parseInt)
            .max()
            .ifPresent(max -> bookingCounter = max + 1);

        System.out.printf("  Data loaded: %d vehicle(s), %d user(s), %d booking(s).%n",
            vehicles.size(), users.size(), bookings.size());
    }

    /** Save all data to .dat files before exit. */
    public void saveData() {
        FileHandler.saveVehicles(vehicles);
        FileHandler.saveUsers(users);
        FileHandler.saveBookings(bookings);
        System.out.println("  Data saved successfully.");
    }

    // ─── Seed sample data (only if no data loaded) ───────────────────────────
    public void seedSampleData() {
        if (!vehicles.isEmpty() || !users.isEmpty()) return; // already have data

        // Sample vehicles
        vehicles.add(new com.rental.model.Car("C001", "Maruti Swift",        5, 1200, "Petrol"));
        vehicles.add(new com.rental.model.Car("C002", "Honda City",           5, 1800, "Petrol"));
        vehicles.add(new com.rental.model.Car("C003", "Tata Nexon EV",        5, 2200, "Electric"));
        vehicles.add(new com.rental.model.Bike("B001", "Royal Enfield 350",   2,  600, 350));
        vehicles.add(new com.rental.model.Bike("B002", "Honda Activa",        2,  300, 110));
        vehicles.add(new com.rental.model.SUV("S001",  "Toyota Fortuner",     7, 3500, true));
        vehicles.add(new com.rental.model.SUV("S002",  "Mahindra Thar",       4, 2800, false));

        // Sample users
        users.add(new User("U001", "Alice Sharma",  "alice@email.com",  "9876543210", "alice123"));
        users.add(new User("U002", "Bob Verma",     "bob@email.com",    "9123456789", "bob123"));
        users.add(new User("U003", "Carol Nair",    "carol@email.com",  "9988776655", "carol123"));

        System.out.println("  Sample data initialized.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  USER MANAGEMENT
    // ═══════════════════════════════════════════════════════════════════════════

    public User login(String email, String password) {
        return users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.checkPassword(password))
            .findFirst()
            .orElse(null);
    }

    public void registerUser(User user) throws Exception {
        boolean emailExists = users.stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
        if (emailExists) throw new Exception("Email already registered: " + user.getEmail());
        users.add(user);
    }

    public List<User> getAllUsers() { return Collections.unmodifiableList(users); }

    // ═══════════════════════════════════════════════════════════════════════════
    //  VEHICLE QUERIES
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    /** Returns only AVAILABLE vehicles, sorted by price ascending. */
    public List<Vehicle> getAvailableVehicles() {
        return vehicles.stream()
            .filter(Vehicle::isAvailable)
            .sorted()
            .collect(Collectors.toList());
    }

    /** Filter by type (case-insensitive). */
    public List<Vehicle> searchByType(String type) {
        return vehicles.stream()
            .filter(v -> v.getType().equalsIgnoreCase(type) && v.isAvailable())
            .collect(Collectors.toList());
    }

    /** Filter by max price per day. */
    public List<Vehicle> filterByMaxPrice(double maxPrice) {
        return vehicles.stream()
            .filter(v -> v.getPricePerDay() <= maxPrice && v.isAvailable())
            .sorted()
            .collect(Collectors.toList());
    }

    public Optional<Vehicle> findVehicleById(String id) {
        return vehicles.stream().filter(v -> v.getVehicleId().equalsIgnoreCase(id)).findFirst();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  RENTABLE INTERFACE IMPLEMENTATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Books a vehicle for the given user and date range.
     * Validates availability and date conflicts before committing.
     */
    @Override
    public String book(User user, LocalDate startDate, LocalDate endDate) throws Exception {
        throw new UnsupportedOperationException("Use bookVehicle(vehicleId, user, start, end)");
    }

    /**
     * Full booking flow with vehicleId.
     */
    public String bookVehicle(String vehicleId, User user,
                               LocalDate startDate, LocalDate endDate) throws Exception {
        // 1. Validate date range
        if (!endDate.isAfter(startDate)) {
            throw new Exception("End date must be after start date.");
        }

        // 2. Find vehicle
        Vehicle vehicle = findVehicleById(vehicleId)
            .orElseThrow(() -> new Exception("Vehicle not found: " + vehicleId));

        // 3. Check general availability flag
        if (!vehicle.isAvailable()) {
            throw new Exception("Vehicle " + vehicleId + " is currently not available.");
        }

        // 4. Check for date conflicts with existing active bookings
        boolean conflict = bookings.stream()
            .filter(b -> b.getVehicle().getVehicleId().equals(vehicleId))
            .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
            .anyMatch(b -> b.overlapsWith(startDate, endDate));

        if (conflict) {
            throw new Exception("Vehicle is already booked for the selected dates.");
        }

        // 5. Create booking (polymorphic calculateRent() called inside Booking constructor)
        String bookingId = "BK" + String.format("%04d", bookingCounter++);
        Booking booking  = new Booking(bookingId, user, vehicle, startDate, endDate);
        bookings.add(booking);

        // 6. Mark vehicle as unavailable if no future availability (simple flag)
        vehicle.setAvailable(false);

        return bookingId;
    }

    /**
     * Cancel an active booking and free the vehicle.
     */
    @Override
    public void cancel(String bookingId) throws Exception {
        Booking booking = bookings.stream()
            .filter(b -> b.getBookingId().equals(bookingId))
            .findFirst()
            .orElseThrow(() -> new Exception("Booking not found: " + bookingId));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new Exception("Only ACTIVE bookings can be cancelled. Current status: "
                + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);

        // Release the vehicle if no other active bookings overlap
        String vid = booking.getVehicle().getVehicleId();
        boolean stillBooked = bookings.stream()
            .filter(b -> b.getVehicle().getVehicleId().equals(vid))
            .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
            .anyMatch(b -> b.overlapsWith(LocalDate.now(), LocalDate.now().plusYears(5)));

        booking.getVehicle().setAvailable(!stillBooked);
    }

    @Override
    public boolean checkAvailability(LocalDate startDate, LocalDate endDate) {
        // Generic: returns true if ANY vehicle is available
        return vehicles.stream().anyMatch(Vehicle::isAvailable);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BOOKING QUERIES
    // ═══════════════════════════════════════════════════════════════════════════

    /** All bookings for a given user. */
    public List<Booking> getBookingsByUser(String userId) {
        return bookings.stream()
            .filter(b -> b.getUser().getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    /** All bookings for a given vehicle. */
    public List<Booking> getBookingsByVehicle(String vehicleId) {
        return bookings.stream()
            .filter(b -> b.getVehicle().getVehicleId().equals(vehicleId))
            .collect(Collectors.toList());
    }

    public List<Booking> getAllBookings() {
        return Collections.unmodifiableList(bookings);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MANAGEABLE INTERFACE IMPLEMENTATION
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    public void addVehicle(Vehicle vehicle) {
        // Prevent duplicate IDs
        boolean exists = vehicles.stream()
            .anyMatch(v -> v.getVehicleId().equalsIgnoreCase(vehicle.getVehicleId()));
        if (exists) throw new IllegalArgumentException(
            "Vehicle ID already exists: " + vehicle.getVehicleId());
        vehicles.add(vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) throws Exception {
        Vehicle vehicle = findVehicleById(vehicleId)
            .orElseThrow(() -> new Exception("Vehicle not found: " + vehicleId));

        // Safety check — do not remove if there are active bookings
        boolean hasActive = bookings.stream()
            .filter(b -> b.getVehicle().getVehicleId().equals(vehicleId))
            .anyMatch(b -> b.getStatus() == BookingStatus.ACTIVE);
        if (hasActive) throw new Exception("Cannot remove vehicle with active bookings.");

        vehicles.remove(vehicle);
    }

    /**
     * Update a vehicle field.
     * Supported fields: name, pricePerDay, seats, available
     */
    @Override
    public void updateVehicle(String vehicleId, String field, String value) throws Exception {
        Vehicle v = findVehicleById(vehicleId)
            .orElseThrow(() -> new Exception("Vehicle not found: " + vehicleId));

        switch (field.toLowerCase()) {
            case "name":
                v.setName(value);
                break;
            case "priceperday":
            case "price":
                v.setPricePerDay(Double.parseDouble(value));
                break;
            case "seats":
                v.setSeats(Integer.parseInt(value));
                break;
            case "available":
                v.setAvailable(Boolean.parseBoolean(value));
                break;
            default:
                throw new Exception("Unknown field: " + field
                    + ". Supported: name, price, seats, available");
        }
    }
}
