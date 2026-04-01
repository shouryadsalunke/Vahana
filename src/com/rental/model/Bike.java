package com.rental.model;

/**
 * Bike — extends Vehicle.
 * Bikes are cheaper and carry fewer passengers.
 * Applies a small surcharge for single-day rentals.
 */
public class Bike extends Vehicle {

    private static final long serialVersionUID = 3L;

    private int engineCC; // Engine displacement in CC

    public Bike(String vehicleId, String name, int seats, double pricePerDay, int engineCC) {
        super(vehicleId, name, "BIKE", seats, pricePerDay);
        this.engineCC = engineCC;
    }

    /**
     * Bikes have a flat ₹50 surcharge for single-day rentals (short trips cost more).
     */
    @Override
    public double calculateRent(int days) {
        double base = days * getPricePerDay();
        if (days == 1) {
            base += 50; // single-day handling surcharge
        }
        return base;
    }

    public int  getEngineCC()          { return engineCC; }
    public void setEngineCC(int cc)    { this.engineCC = cc; }

    @Override
    public String toString() {
        return super.toString() + " | Engine: " + engineCC + "cc";
    }
}
