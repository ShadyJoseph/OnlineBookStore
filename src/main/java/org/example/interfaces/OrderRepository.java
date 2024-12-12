package org.example.interfaces;

import org.example.models.Order;

import java.util.List;

public interface OrderRepository {
    void saveOrder(Order order);
    List<Order> getOrdersByCustomerId(int customerId);
    void updateOrder(Order order);
    Order getOrderById(int orderId); // Added method for retrieving a single order
}
