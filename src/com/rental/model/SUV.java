package com.rental.model;

/**
 * SUV — extends Vehicle.
 * SUVs are premium vehicles with a 10% luxury surcharge.
 */
public class SUV extends Vehicle {

    private static final long serialVersionUID = 4L;

    private boolean hasSunroof;

    public SUV(String vehicleId, String name, int seats, double pricePerDay, boolean hasSunroof) {
        super(vehicleId, name, "SUV", seats, pricePerDay);
        this.hasSunroof = hasSunroof;
    }

    /**
     * SUVs carry a 10% luxury surcharge on every rental.
     */
    @Override
    public double calculateRent(int days) {
        double base = days * getPricePerDay();
        base *= 1.10; // 10% luxury surcharge
        return base;
    }

    public boolean hasSunroof()                { return hasSunroof; }
    public void    setHasSunroof(boolean flag) { this.hasSunroof = flag; }

    @Override
    public String toString() {
        return super.toString() + " | Sunroof: " + (hasSunroof ? "Yes" : "No");
    }
}
