package org.example.services;

import org.example.enums.UserRole;
import org.example.models.Admin;
import org.example.models.Customer;
import org.example.models.User;
import org.example.models.UserFactory;
import org.example.utils.HashUtil;
import org.example.utils.ValidationUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final String USERS_FILE_PATH = "src/main/resources/users.txt";
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_ADMIN = "ADMIN";

    private final List<User> users = new ArrayList<>();
    private final AtomicInteger userIdCounter = new AtomicInteger(1);

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserService() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        File file = new File(USERS_FILE_PATH);
        if (!file.exists()) {
            LOGGER.log(Level.WARNING, "Users file does not exist: {0}", USERS_FILE_PATH);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    parseAndAddUser(line);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Skipping malformed line: {0}", line);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading users file: {0}", e.getMessage());
        }
    }

    private void parseAndAddUser(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) throw new IllegalArgumentException("Invalid data format");

        int id = Integer.parseInt(parts[0].trim());
        String username = parts[1].trim();
        String password = parts[2].trim();
        String role = parts[3].trim();

        UserRole userRole = UserRole.valueOf(role); // Convert role string to enum

        if (userRole == UserRole.CUSTOMER) {
            if (parts.length < 6) throw new IllegalArgumentException("Incomplete customer data");
            String address = parts[4].trim();
            String phone = parts[5].trim();
            users.add(UserFactory.createUser(id, username, password, userRole, address, phone));
        } else if (userRole == UserRole.ADMIN) {
            users.add(UserFactory.createUser(id, username, password, userRole, null, null));
        } else {
            throw new IllegalArgumentException("Unknown role: " + role);
        }

        userIdCounter.set(Math.max(userIdCounter.get(), id + 1));
    }


    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH))) {
            synchronized (users) {
                for (User user : users) {
                    writer.write(user.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving users to file: {0}", e.getMessage());
        }
    }

    public synchronized boolean signUp(String username, String password, String address, String phone) {
        if (!validateSignUpData(username, password, address, phone)) return false;

        if (isUsernameTaken(username)) {
            LOGGER.log(Level.WARNING, "Attempt to sign up with an existing username: {0}", username);
            return false;
        }

        String hashedPassword = HashUtil.hashPassword(password);
        // Assuming "CUSTOMER" role for signUp
        users.add(UserFactory.createUser(userIdCounter.getAndIncrement(), username, hashedPassword, UserRole.CUSTOMER, address, phone));
        saveUsersToFile();
        return true;
    }


    public synchronized User logIn(String username, String password) {
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && HashUtil.verifyPassword(password, u.getPassword()))
                .findFirst();
        if (user.isEmpty()) {
            LOGGER.log(Level.INFO, "Failed login attempt for username: {0}", username);
        }
        return user.orElse(null);
    }

    public synchronized void displayAllUsers() {
        users.forEach(System.out::println);
    }

    // Helper Methods
    private boolean validateSignUpData(String username, String password, String address, String phone) {
        if (!ValidationUtil.isValidUsername(username)) {
            LOGGER.log(Level.WARNING, "Invalid username: {0}", username);
            return false;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            LOGGER.log(Level.WARNING, "Invalid password for username: {0}", username);
            return false;
        }
        if (!ValidationUtil.isValidAddress(address)) {
            LOGGER.log(Level.WARNING, "Invalid address for username: {0}", username);
            return false;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            LOGGER.log(Level.WARNING, "Invalid phone for username: {0}", username);
            return false;
        }
        return true;
    }

    private boolean isUsernameTaken(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }
}
