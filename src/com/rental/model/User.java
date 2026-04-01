package com.rental.model;

import java.io.Serializable;

/**
 * Represents a registered customer in the rental system.
 * Encapsulation: all fields private with getters/setters.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5L;

    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password; // stored as plain-text for demo; hash in production

    public User(String userId, String name, String email, String phone, String password) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.password = password;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────
    public String getUserId()                  { return userId; }
    public void   setUserId(String id)         { this.userId = id; }

    public String getName()                    { return name; }
    public void   setName(String name)         { this.name = name; }

    public String getEmail()                   { return email; }
    public void   setEmail(String email)       { this.email = email; }

    public String getPhone()                   { return phone; }
    public void   setPhone(String phone)       { this.phone = phone; }

    public String getPassword()                { return password; }
    public void   setPassword(String pwd)      { this.password = pwd; }

    public boolean checkPassword(String input) { return this.password.equals(input); }

    @Override
    public String toString() {
        return String.format("[%s] %-20s | Email: %-30s | Phone: %s",
            userId, name, email, phone);
    }
}
