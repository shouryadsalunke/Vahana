package com.rental.util;

import com.rental.model.Booking;
import com.rental.model.User;
import com.rental.model.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file I/O for the rental system using Java Serialization.
 *
 * Files:
 *   vehicles.dat  — serialized ArrayList<Vehicle>
 *   users.dat     — serialized ArrayList<User>
 *   bookings.dat  — serialized ArrayList<Booking>
 */
public class FileHandler {

    // File paths (relative to working directory)
    private static final String DATA_DIR      = "data/";
    private static final String VEHICLES_FILE = DATA_DIR + "vehicles.dat";
    private static final String USERS_FILE    = DATA_DIR + "users.dat";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.dat";

    // Static initialiser — ensure data directory exists
    static {
        new File(DATA_DIR).mkdirs();
    }

    // ─── Save methods ─────────────────────────────────────────────────────────

    /** Serializes the full vehicle list to disk. */
    public static void saveVehicles(List<Vehicle> vehicles) {
        saveObject(vehicles, VEHICLES_FILE);
    }

    /** Serializes the full user list to disk. */
    public static void saveUsers(List<User> users) {
        saveObject(users, USERS_FILE);
    }

    /** Serializes the full booking list to disk. */
    public static void saveBookings(List<Booking> bookings) {
        saveObject(bookings, BOOKINGS_FILE);
    }

    // ─── Load methods ─────────────────────────────────────────────────────────

    /** Loads and returns the vehicle list, or an empty list if not found. */
    @SuppressWarnings("unchecked")
    public static List<Vehicle> loadVehicles() {
        Object obj = loadObject(VEHICLES_FILE);
        return (obj instanceof List) ? (List<Vehicle>) obj : new ArrayList<>();
    }

    /** Loads and returns the user list, or an empty list if not found. */
    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        Object obj = loadObject(USERS_FILE);
        return (obj instanceof List) ? (List<User>) obj : new ArrayList<>();
    }

    /** Loads and returns the booking list, or an empty list if not found. */
    @SuppressWarnings("unchecked")
    public static List<Booking> loadBookings() {
        Object obj = loadObject(BOOKINGS_FILE);
        return (obj instanceof List) ? (List<Booking>) obj : new ArrayList<>();
    }

    // ─── Generic helpers ──────────────────────────────────────────────────────

    private static void saveObject(Object obj, String filePath) {
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("[FileHandler] ERROR saving to " + filePath + ": " + e.getMessage());
        }
    }

    private static Object loadObject(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null; // First run — no data yet
        }
        try (ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("[FileHandler] File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("[FileHandler] IO error reading " + filePath + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[FileHandler] Class mismatch in " + filePath + ": " + e.getMessage());
        }
        return null;
    }
}
