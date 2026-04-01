package com.rental.factory;

import com.rental.model.Bike;
import com.rental.model.Car;
import com.rental.model.SUV;
import com.rental.model.Vehicle;

import java.util.UUID;

/**
 * Factory Pattern implementation for creating Vehicle objects.
 * Centralises object creation and avoids scattered 'new' calls.
 *
 * Usage: VehicleFactory.createVehicle("CAR", name, seats, price, extra)
 */
public class VehicleFactory {

    // Private constructor — utility class, not meant to be instantiated
    private VehicleFactory() {}

    /**
     * Creates a Vehicle of the requested type.
     *
     * @param type        "CAR", "BIKE", or "SUV" (case-insensitive)
     * @param name        Display name of the vehicle
     * @param seats       Number of passenger seats
     * @param pricePerDay Base rental price per day
     * @param extra       Type-specific parameter:
     *                      CAR  → fuel type (String, e.g. "Petrol")
     *                      BIKE → engine CC (String, numeric, e.g. "150")
     *                      SUV  → hasSunroof (String "true"/"false")
     * @return            A concrete Vehicle subclass instance
     * @throws IllegalArgumentException for unknown types
     */
    public static Vehicle createVehicle(String type, String name,
                                        int seats, double pricePerDay,
                                        String extra) {
        String id = generateId(type);

        switch (type.toUpperCase()) {
            case "CAR":
                String fuelType = (extra != null && !extra.isBlank()) ? extra : "Petrol";
                return new Car(id, name, seats, pricePerDay, fuelType);

            case "BIKE":
                int cc = 150;
                try { cc = Integer.parseInt(extra); } catch (NumberFormatException ignored) {}
                return new Bike(id, name, seats, pricePerDay, cc);

            case "SUV":
                boolean sunroof = "true".equalsIgnoreCase(extra);
                return new SUV(id, name, seats, pricePerDay, sunroof);

            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }

    /**
     * Overload for internal use with a known ID (e.g., deserialization migration).
     */
    public static Vehicle createVehicle(String type, String id, String name,
                                        int seats, double pricePerDay, String extra) {
        switch (type.toUpperCase()) {
            case "CAR":
                return new Car(id, name, seats, pricePerDay,
                               extra != null ? extra : "Petrol");
            case "BIKE":
                int cc = 150;
                try { cc = Integer.parseInt(extra); } catch (NumberFormatException ignored) {}
                return new Bike(id, name, seats, pricePerDay, cc);
            case "SUV":
                return new SUV(id, name, seats, pricePerDay,
                               "true".equalsIgnoreCase(extra));
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }

    // ─── ID generation ───────────────────────────────────────────────────────
    private static int counter = 1;

    private static String generateId(String type) {
        // e.g., CAR-001, BIKE-002, SUV-003
        return type.toUpperCase().substring(0, 1)
               + String.format("%03d", counter++);
    }
}
