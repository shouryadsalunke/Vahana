package com.rental.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a vehicle booking (rental transaction).
 * Uses LocalDate for proper date handling.
 */
public class Booking implements Serializable {

    private static final long serialVersionUID = 6L;

    private String        bookingId;
    private User          user;
    private Vehicle       vehicle;
    private LocalDate     startDate;
    private LocalDate     endDate;
    private double        totalCost;
    private BookingStatus status;

    public Booking(String bookingId, User user, Vehicle vehicle,
                   LocalDate startDate, LocalDate endDate) {
        this.bookingId = bookingId;
        this.user      = user;
        this.vehicle   = vehicle;
        this.startDate = startDate;
        this.endDate   = endDate;
        this.status    = BookingStatus.ACTIVE;

        // Polymorphic call — uses the overridden calculateRent() of actual type
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        this.totalCost = vehicle.calculateRent(days);
    }

    // ─── Date overlap check — used to prevent double booking ─────────────────
    /**
     * Returns true if this booking overlaps with the proposed [from, to] range.
     */
    public boolean overlapsWith(LocalDate from, LocalDate to) {
        // Overlap exists unless one range ends before the other starts
        return !endDate.isBefore(from) && !to.isBefore(startDate);
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────
    public String        getBookingId()               { return bookingId; }
    public void          setBookingId(String id)      { this.bookingId = id; }

    public User          getUser()                    { return user; }
    public void          setUser(User u)              { this.user = u; }

    public Vehicle       getVehicle()                 { return vehicle; }
    public void          setVehicle(Vehicle v)        { this.vehicle = v; }

    public LocalDate     getStartDate()               { return startDate; }
    public void          setStartDate(LocalDate d)    { this.startDate = d; }

    public LocalDate     getEndDate()                 { return endDate; }
    public void          setEndDate(LocalDate d)      { this.endDate = d; }

    public double        getTotalCost()               { return totalCost; }
    public void          setTotalCost(double cost)    { this.totalCost = cost; }

    public BookingStatus getStatus()                  { return status; }
    public void          setStatus(BookingStatus s)   { this.status = s; }

    @Override
    public String toString() {
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        return String.format(
            "Booking[%s] | User: %-15s | Vehicle: %-20s | %s → %s (%d days) | ₹%.2f | %s",
            bookingId, user.getName(), vehicle.getName(),
            startDate, endDate, days, totalCost, status
        );
    }
}
