package com.rental.model;

/**
 * Interface defining management operations.
 * Demonstrates INTERFACE usage.
 */
public interface Manageable {

    void addVehicle(Vehicle vehicle);

    void removeVehicle(String vehicleId) throws Exception;

    void updateVehicle(String vehicleId, String field, String value) throws Exception;
}
