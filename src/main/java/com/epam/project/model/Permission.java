package com.epam.project.model;

// Wyliczenie praw
public enum Permission {
    //Uprawnienia zawierające atrybuty uprawnień określonej roli
    READ("read"),
    ADMIN_WRITE("admin:write"),
    EMPLOYEE_WRITE("employee:write");

    // Uprawnienia obiektu
    private final String permission;

    public String getPermission() {
        return permission;
    }

    Permission(String permission) {
        this.permission = permission;
    }
}
