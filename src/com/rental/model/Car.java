package com.rental.model;

/**
 * Car — extends Vehicle.
 * Demonstrates INHERITANCE and POLYMORPHISM.
 * Applies a 5% weekend-simulation discount on long rentals (7+ days).
 */
public class Car extends Vehicle {

    private static final long serialVersionUID = 2L;

    private String fuelType; // e.g., Petrol, Diesel, Electric

    public Car(String vehicleId, String name, int seats, double pricePerDay, String fuelType) {
        super(vehicleId, name, "CAR", seats, pricePerDay);
        this.fuelType = fuelType;
    }

    /**
     * Cars get a 5% discount for rentals of 7 days or more.
     */
    @Override
    public double calculateRent(int days) {
        double base = days * getPricePerDay();
        if (days >= 7) {
            base *= 0.95; // 5% long-rental discount
        }
        return base;
    }

    public String getFuelType()              { return fuelType; }
    public void   setFuelType(String fuel)   { this.fuelType = fuel; }

    @Override
    public String toString() {
        return super.toString() + " | Fuel: " + fuelType;
    }
}
