package org.example.ui;
import org.example.utils.ValidationUtil;

import org.example.models.Customer;
import org.example.services.CustomerService;

import java.util.Scanner;

public class CustomerUI {
    private final CustomerService customerService = new CustomerService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("Welcome to the Online Book Store!");
        while (true) {
            System.out.println("\n1. Sign Up\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> signUp();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Thank you for visiting!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void signUp() {
        System.out.println("Sign Up:");
        Customer customer = new Customer();

        System.out.print("Username: ");
        String username = scanner.nextLine();
        if (!ValidationUtil.isValidUsername(username)) {
            System.out.println("Invalid username. Must be 50 characters or fewer.");
            return;
        }
        customer.setUsername(username);

        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (!ValidationUtil.isValidPassword(password)) {
            System.out.println("Invalid password. Must be at least 8 characters.");
            return;
        }
        customer.setPassword(password);

        System.out.print("Address: ");
        String address = scanner.nextLine();
        if (!ValidationUtil.isValidAddress(address)) {
            System.out.println("Invalid address. Must be 255 characters or fewer.");
            return;
        }
        customer.setAddress(address);

        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        if (!ValidationUtil.isValidPhone(phone)) {
            System.out.println("Invalid phone number. Must be 10-15 digits.");
            return;
        }
        customer.setPhone(phone);

        if (customerService.signUp(customer)) {
            System.out.println("Sign-up successful!");
        } else {
            System.out.println("Sign-up failed. Please try again.");
        }
    }


    private void login() {
        System.out.println("Login:");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Customer customer = customerService.login(username, password);
        if (customer != null) {
            System.out.println("Login successful! Welcome, " + customer.getUsername());
            while (true) {
                System.out.println("\n1. Update Account\n2. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (choice == 1) {
                    updateAccount(customer);
                } else if (choice == 2) {
                    System.out.println("Logged out successfully.");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }


    private void updateAccount(Customer customer) {
        System.out.println("\nUpdate Account:");
        System.out.print("New Address: ");
        customer.setAddress(scanner.nextLine());
        System.out.print("New Phone: ");
        customer.setPhone(scanner.nextLine());
        System.out.print("New Password: ");
        customer.setPassword(scanner.nextLine());

        if (customerService.updateCustomerDetails(customer)) {
            System.out.println("Account updated successfully!");
        } else {
            System.out.println("Failed to update account.");
        }
    }
}
