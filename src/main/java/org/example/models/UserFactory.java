package org.example.models;

import org.example.enums.UserRole;

public class UserFactory {
    public static User createUser(int id, String username, String password, UserRole role, String address, String phone) {
        switch (role) {
            case ADMIN:
                return new Admin(id, username, password);
            case CUSTOMER:
                return new Customer(id, username, password, address, phone);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
