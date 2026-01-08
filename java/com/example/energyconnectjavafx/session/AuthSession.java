package com.example.energyconnectjavafx.session;

public class AuthSession {

    private static Integer loggedInAdminId;
    private static Integer setupAdminId;
    private static Integer resetAdminId;

    public static void login(int adminId) {
        loggedInAdminId = adminId;
    }

    public static Integer getLoggedInAdminId() {
        return loggedInAdminId;
    }

    public static void setSetupAdminId(int id) {
        setupAdminId = id;
    }

    public static Integer getSetupAdminId() {
        return setupAdminId;
    }

    public static void clearSetup() {
        setupAdminId = null;
    }

    public static void setResetAdminId(int id) {
        resetAdminId = id;
    }

    public static Integer getResetAdminId() {
        return resetAdminId;
    }

    public static void clearReset() {
        resetAdminId = null;
    }
}
