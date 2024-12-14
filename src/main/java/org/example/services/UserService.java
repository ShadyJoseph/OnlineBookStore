package org.example.services;

import org.example.models.Customer;
import org.example.models.User;
import org.example.models.Admin;
import org.example.utils.HashUtil;
import org.example.utils.ValidationUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final String usersFilePath = "src/main/resources/users.txt";
    private final List<User> users = new ArrayList<>();

    public UserService() {
        loadUsersFromFile();
    }

    // Load users from file
    private void loadUsersFromFile() {
        File file = new File(usersFilePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String role = parts[4];
                    if (role.equals("CUSTOMER")) {
                        Customer customer = new Customer(parts[1], parts[2], parts[3], parts[4]);
                        users.add(customer);
                    } else if (role.equals("ADMIN")) {
                        Admin admin = new Admin(parts[1], parts[2]);
                        users.add(admin);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save users to file
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFilePath))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sign Up
    public boolean signUp(String username, String password, String address, String phone) {
        if (!ValidationUtil.isValidUsername(username)) {
            System.out.println("Invalid username. Must be alphanumeric and 50 characters or less.");
            return false;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            System.out.println("Invalid password. Must be at least 8 characters long, include an uppercase letter, and a special character.");
            return false;
        }

        if (!ValidationUtil.isValidAddress(address)) {
            System.out.println("Invalid address. Must be 255 characters or less.");
            return false;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            System.out.println("Invalid phone number. Must be between 10 and 15 digits.");
            return false;
        }

        Optional<User> existingUser = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
        if (existingUser.isPresent()) {
            System.out.println("Username already exists.");
            return false;
        }

        String hashedPassword = HashUtil.hashPassword(password);
        Customer newCustomer = new Customer(username, hashedPassword, address, phone);
        users.add(newCustomer);
        saveUsersToFile();
        System.out.println("Sign up successful for user: " + username);
        return true;
    }

    // Log In
    public User logIn(String username, String password) {
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (user.isPresent()) {
            boolean isPasswordValid = HashUtil.verifyPassword(password, user.get().getPassword());
            if (isPasswordValid) {
                System.out.println("Login successful for user: " + username);
                return user.get();
            } else {
                System.out.println("Invalid password.");
                return null;
            }
        } else {
            System.out.println("Invalid username.");
            return null;
        }
    }

    // Update Account Details
    public boolean updateAccountDetails(int userId, String newUsername, String newPassword, String newAddress, String newPhone) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst();
        if (user.isPresent() && user.get() instanceof Customer) {
            Customer customer = (Customer) user.get();
            if (newUsername != null && !ValidationUtil.isValidUsername(newUsername)) {
                System.out.println("Invalid username. Must be alphanumeric and 50 characters or less.");
                return false;
            }

            if (newPassword != null && !ValidationUtil.isValidPassword(newPassword)) {
                System.out.println("Invalid password. Must be at least 8 characters long, include an uppercase letter, and a special character.");
                return false;
            }

            if (newAddress != null && !ValidationUtil.isValidAddress(newAddress)) {
                System.out.println("Invalid address. Must be 255 characters or less.");
                return false;
            }

            if (newPhone != null && !ValidationUtil.isValidPhone(newPhone)) {
                System.out.println("Invalid phone number. Must be between 10 and 15 digits.");
                return false;
            }

            customer.setUsername(newUsername != null ? newUsername : customer.getUsername());
            customer.setPassword(newPassword != null ? HashUtil.hashPassword(newPassword) : customer.getPassword());
            customer.setAddress(newAddress != null ? newAddress : customer.getAddress());
            customer.setPhone(newPhone != null ? newPhone : customer.getPhone());

            saveUsersToFile();
            System.out.println("Account details updated for user ID: " + userId);
            return true;
        } else {
            System.out.println("User not found or invalid operation.");
            return false;
        }
    }

    // Display All Users (for admin purposes or debugging)
    public void displayAllUsers() {
        users.forEach(System.out::println);
    }
}
