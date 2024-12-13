package org.example.models;

import org.example.enums.UserRole;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, UserRole.ADMIN);
    }

    public Admin() {
        super();
    }

    @Override
    public void displayCapabilities() {
        System.out.println("Admin Capabilities: Manage Books, Categories, and Users.");
    }
}