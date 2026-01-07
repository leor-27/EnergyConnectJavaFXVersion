package com.example.energyconnectjavafx.Model;

public class Admin {

    private final int id;
    private final String passwordHash;
    private final boolean initialized;

    public Admin(int id, String passwordHash, boolean initialized) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.initialized = initialized;
    }

    public int getId() {
        return id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
