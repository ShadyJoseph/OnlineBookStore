package org.example.services;

import org.example.models.Customer;
import org.example.utils.HashUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final String CUSTOMER_FILE = "src/main/resources/customers.txt";

    public boolean signUp(Customer customer) {
        List<Customer> customers = readCustomersFromFile();
        for (Customer existingCustomer : customers) {
            if (existingCustomer.getUsername().equalsIgnoreCase(customer.getUsername())) {
                return false; // Username already exists
            }
        }
        customer.setPassword(HashUtil.hashPassword(customer.getPassword()));
        customer.setId(customers.size() + 1); // Auto-increment ID
        customers.add(customer);
        return writeCustomersToFile(customers);
    }

    public Customer login(String username, String password) {
        List<Customer> customers = readCustomersFromFile();
        for (Customer customer : customers) {
            if (customer.getUsername().equalsIgnoreCase(username) &&
                    HashUtil.verifyPassword(password, customer.getPassword())) {
                return customer;
            }
        }
        return null; // Login failed
    }

    public boolean updateCustomerDetails(Customer updatedCustomer) {
        List<Customer> customers = readCustomersFromFile();
        for (Customer customer : customers) {
            if (customer.getId() == updatedCustomer.getId()) {
                customer.setAddress(updatedCustomer.getAddress());
                customer.setPhone(updatedCustomer.getPhone());
                customer.setPassword(HashUtil.hashPassword(updatedCustomer.getPassword()));
                return writeCustomersToFile(customers);
            }
        }
        return false;
    }

    private List<Customer> readCustomersFromFile() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Initialize file if it doesn't exist
            } catch (IOException e) {
                System.err.println("Error initializing customer file: " + e.getMessage());
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Customer customer = new Customer();
                    customer.setId(Integer.parseInt(parts[0]));
                    customer.setUsername(parts[1]);
                    customer.setPassword(parts[2]);
                    customer.setAddress(parts[3]);
                    customer.setPhone(parts[4]);
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customers from file: " + e.getMessage());
        }
        return customers;
    }

    private boolean writeCustomersToFile(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customers) {
                writer.write(customer.getId() + "," + customer.getUsername() + "," +
                        customer.getPassword() + "," + customer.getAddress() + "," +
                        customer.getPhone() + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing customers to file: " + e.getMessage());
            return false;
        }
    }
}
