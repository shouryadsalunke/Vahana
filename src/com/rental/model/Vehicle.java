package com.rental.model;

import java.io.Serializable;

/**
 * Abstract base class for all vehicle types.
 * Demonstrates ABSTRACTION and ENCAPSULATION.
 */
public abstract class Vehicle implements Serializable, Comparable<Vehicle> {

    private static final long serialVersionUID = 1L;

    // Encapsulated fields — all private
    private String vehicleId;
    private String name;
    private String type;
    private int seats;
    private double pricePerDay;
    private boolean available;

    // Constructor
    public Vehicle(String vehicleId, String name, String type, int seats, double pricePerDay) {
        this.vehicleId   = vehicleId;
        this.name        = name;
        this.type        = type;
        this.seats       = seats;
        this.pricePerDay = pricePerDay;
        this.available   = true;
    }

    // ─── Abstract method — subclasses MUST override (Polymorphism) ───────────
    /**
     * Calculates total rental cost for the given number of days.
     * Each vehicle type may apply its own pricing logic.
     */
    public abstract double calculateRent(int days);

    // ─── Getters & Setters ───────────────────────────────────────────────────
    public String getVehicleId()               { return vehicleId; }
    public void   setVehicleId(String id)      { this.vehicleId = id; }

    public String getName()                    { return name; }
    public void   setName(String name)         { this.name = name; }

    public String getType()                    { return type; }
    public void   setType(String type)         { this.type = type; }

    public int    getSeats()                   { return seats; }
    public void   setSeats(int seats)          { this.seats = seats; }

    public double getPricePerDay()             { return pricePerDay; }
    public void   setPricePerDay(double price) { this.pricePerDay = price; }

    public boolean isAvailable()               { return available; }
    public void    setAvailable(boolean flag)  { this.available = flag; }

    // ─── Comparable — sort by price ──────────────────────────────────────────
    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.pricePerDay, other.pricePerDay);
    }

    // ─── Display helper ──────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "[%s] %-20s | Type: %-5s | Seats: %2d | ₹%.2f/day | %s",
            vehicleId, name, type, seats, pricePerDay,
            available ? "AVAILABLE" : "BOOKED"
        );
    }
}
