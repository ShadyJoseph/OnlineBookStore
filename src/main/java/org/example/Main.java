package org.example;

import org.example.models.User;
import org.example.services.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();

        // Create Admin user
        User adminUser = userService.signUp("joseph", "Joseph123!");
        if (adminUser != null) {
            System.out.println("Admin user created: " + adminUser);
        } else {
            System.out.println("Failed to create Admin user.");
        }

        // Create Customer user
        User customerUser = userService.signUp("customerUsername", "customerPassword!", "123 Street, City", "1234567890");
        if (customerUser != null) {
            System.out.println("Customer user created: " + customerUser);
        } else {
            System.out.println("Failed to create Customer user.");
        }

        // Optionally, display all users after creation
        System.out.println("All Users:");
        userService.displayAllUsers();
    }
}
