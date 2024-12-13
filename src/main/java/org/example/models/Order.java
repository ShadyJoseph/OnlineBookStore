package org.example.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private List<CartItem> items;
    private String status;
    private String deliveryAddress;
    private LocalDateTime deliveredAt;

    // Default constructor
    public Order() {}

    // Constructor with parameters
    public Order(int customerId, LocalDateTime orderDate, double totalAmount,
                 List<CartItem> items, String status, String deliveryAddress) {
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    // Helper method to calculate the total amount of the order based on CartItem prices and quantities
    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // Method to update order status (useful for admin updates)
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    // Method to check if the order is delivered
    public boolean isDelivered() {
        return this.deliveredAt != null;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                ", status='" + status + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", deliveredAt=" + deliveredAt +
                '}';
    }
}
