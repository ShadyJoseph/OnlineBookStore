package org.example.models;

import org.example.enums.UserRole;

public class Admin extends User {
    public Admin(int id, String username, String password) {
        super(id, username, password, UserRole.ADMIN);
    }

    @Override
    public void displayCapabilities() {
        System.out.println("Admin Capabilities: Manage Books, Categories, and Users.");
    }

    @Override
    public String toFileString() {
        return getId() + "," + getUsername() + "," + getPassword() + ",ADMIN";
    }
}
