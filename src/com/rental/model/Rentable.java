package com.rental.model;

import java.time.LocalDate;

/**
 * Interface defining the contract for any rentable entity.
 * Demonstrates INTERFACE usage.
 */
public interface Rentable {

    /**
     * Book the entity for a given user between the specified dates.
     * Returns the booking ID on success, or throws on failure.
     */
    String book(User user, LocalDate startDate, LocalDate endDate) throws Exception;

    /**
     * Cancel an existing booking by its ID.
     */
    void cancel(String bookingId) throws Exception;

    /**
     * Check whether the entity is available for the requested period.
     */
    boolean checkAvailability(LocalDate startDate, LocalDate endDate);
}
