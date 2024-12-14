package org.example.services;

import org.example.enums.OrderStatus;
import org.example.models.CartItem;
import org.example.models.Order;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {
    private static final String ORDERS_FILE = "src/main/resources/orders.txt";
    private final List<Order> orders = new ArrayList<>();
    private int orderIdCounter = 1; // To ensure unique order IDs

    // Constructor that loads orders from file
    public OrderService() {
        loadOrdersFromFile();
    }

    // Admin Functionalities
    public void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    public boolean confirmOrder(int orderId) {
        Optional<Order> order = findOrderById(orderId);
        if (order.isPresent() && order.get().getStatus().equals(OrderStatus.PENDING.name())) {
            order.get().setStatus(OrderStatus.CONFIRMED.name());
            System.out.println("Order confirmed: " + order.get());
            notifyCustomer(order.get().getCustomerId(), "Your order has been confirmed!");
            saveOrdersToFile(); // Save after modification
            return true;
        }
        System.out.println("Order not found or already processed.");
        return false;
    }

    public boolean cancelOrder(int orderId) {
        Optional<Order> order = findOrderById(orderId);
        if (order.isPresent() && !order.get().getStatus().equals(OrderStatus.DELIVERED.name())) {
            order.get().setStatus(OrderStatus.CANCELLED.name());
            System.out.println("Order cancelled: " + order.get());
            notifyCustomer(order.get().getCustomerId(), "Your order has been cancelled.");
            saveOrdersToFile(); // Save after modification
            return true;
        }
        System.out.println("Order not found or cannot be cancelled.");
        return false;
    }

    // Customer Functionalities
    public Order placeOrder(int customerId, List<CartItem> items, String deliveryAddress) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with empty items.");
        }

        Order newOrder = new Order(
                customerId,
                LocalDateTime.now(),
                0.0,
                items,
                OrderStatus.PENDING.name(),
                deliveryAddress
        );
        newOrder.setId(orderIdCounter++); // Assign a unique ID
        newOrder.calculateTotalAmount();
        orders.add(newOrder);
        System.out.println("Order placed: " + newOrder);
        notifyCustomer(customerId, "Your order has been placed successfully!");
        saveOrdersToFile(); // Save after placing the order
        return newOrder;
    }

    public void trackOrderStatus(int orderId) {
        Optional<Order> order = findOrderById(orderId);
        if (order.isPresent()) {
            System.out.println("Order status: " + order.get().getStatus());
        } else {
            System.out.println("Order not found.");
        }
    }

    public void viewPastOrders(int customerId) {
        List<Order> customerOrders = orders.stream()
                .filter(order -> order.getCustomerId() == customerId)
                .collect(Collectors.toList());

        if (customerOrders.isEmpty()) {
            System.out.println("No past orders found for customer ID: " + customerId);
        } else {
            customerOrders.forEach(System.out::println);
        }
    }

    public boolean cancelOrderByCustomer(int orderId, int customerId) {
        Optional<Order> order = findOrderById(orderId);
        if (order.isPresent() &&
                order.get().getCustomerId() == customerId &&
                order.get().getStatus().equals(OrderStatus.PENDING.name())) {
            order.get().setStatus(OrderStatus.CANCELLED.name());
            System.out.println("Order cancelled by customer: " + order.get());
            saveOrdersToFile(); // Save after modification
            return true;
        }
        System.out.println("Order not found or cannot be cancelled.");
        return false;
    }

    public void leaveReview(int orderId, int customerId, String review) {
        Optional<Order> order = findOrderById(orderId);
        if (order.isPresent() &&
                order.get().getCustomerId() == customerId &&
                order.get().getStatus().equals(OrderStatus.DELIVERED.name())) {
            System.out.println("Review submitted for order " + orderId + ": " + review);
            // Persist review logic (placeholder)
        } else {
            System.out.println("Order not found, not delivered, or invalid operation.");
        }
    }

    // Private Helper Methods
    private Optional<Order> findOrderById(int orderId) {
        return orders.stream().filter(order -> order.getId() == orderId).findFirst();
    }

    private void notifyCustomer(int customerId, String message) {
        // Placeholder: Implement notification system (e.g., email, SMS)
        System.out.println("Notification to customer " + customerId + ": " + message);
    }

    private void loadOrdersFromFile() {
        try {
            Path filePath = Paths.get(ORDERS_FILE);
            if (Files.exists(filePath)) {
                BufferedReader reader = Files.newBufferedReader(filePath);
                String line;
                while ((line = reader.readLine()) != null) {
                    // Implement order deserialization logic (from file to order objects)
                    // For now, just log the line to verify data
                    System.out.println("Loaded order: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    private void saveOrdersToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ORDERS_FILE), StandardOpenOption.CREATE)) {
            for (Order order : orders) {
                writer.write(order.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }
}
