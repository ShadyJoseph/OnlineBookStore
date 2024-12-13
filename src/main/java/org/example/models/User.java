// Abstract User Model
package org.example.models;

import org.example.enums.UserRole;

public abstract class User {
    private static int idCounter = 1;
    private int id;
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.id = idCounter++;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {
        this.id = idCounter++;
    }

    // Getters and Setters
    public int getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public abstract void displayCapabilities();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
