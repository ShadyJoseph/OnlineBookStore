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
        newOrder.calculateTotalAmount();
        orders.add(newOrder);
        System.out.println("Order placed: " + newOrder);
        saveOrdersToFile(); // Save after placing the order
        return newOrder;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String trackOrderStatus(int orderId) {
        Optional<Order> order = findOrderById(orderId);
        if (order.isPresent()) {
            return "Order status: " + order.get().getStatus();
        } else {
            return null;  // Return null if order not found
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

    // Private Helper Methods
    private Optional<Order> findOrderById(int orderId) {
        return orders.stream().filter(order -> order.getId() == orderId).findFirst();
    }

    private void loadOrdersFromFile() {
        try {
            Path filePath = Paths.get(ORDERS_FILE);
            if (Files.exists(filePath)) {
                BufferedReader reader = Files.newBufferedReader(filePath);
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        // Implement order deserialization logic (parse string into Order object)
                        Order order = deserializeOrder(line);
                        orders.add(order);
                    } catch (Exception e) {
                        System.err.println("Error parsing order line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders from file: " + e.getMessage());
        }
    }

    private Order deserializeOrder(String orderString) {
        // Simple deserialization logic, adapt according to your Order format
        String[] parts = orderString.split(",");
        int orderId = Integer.parseInt(parts[0].split("=")[1]);
        int customerId = Integer.parseInt(parts[1].split("=")[1]);
        String status = parts[2].split("=")[1];
        String deliveryAddress = parts[3].split("=")[1];

        // Here, we return a dummy list of CartItems for simplicity. This should be parsed accordingly.
        List<CartItem> items = new ArrayList<>();  // This should be parsed as well, if needed

        return new Order( customerId, LocalDateTime.now(), 0.0, items, status, deliveryAddress);
    }

    private void saveOrdersToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ORDERS_FILE), StandardOpenOption.CREATE)) {
            for (Order order : orders) {
                writer.write(serializeOrder(order) + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error saving orders to file: " + e.getMessage());
        }
    }

    private String serializeOrder(Order order) {
        // Implement order serialization (convert Order object to string)
        return String.format("orderId=%d,customerId=%d,status=%s,deliveryAddress=%s", order.getId(), order.getCustomerId(), order.getStatus(), order.getDeliveryAddress());
    }
}
