package com.example.energyconnectjavafx.Model;

public class Admin {

    private int id;
    private String passwordHash;
    private boolean initialized;

    public Admin(int id, String passwordHash, boolean initialized) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.initialized = initialized;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setIsInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
