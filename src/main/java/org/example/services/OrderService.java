package org.example.services;

import org.example.enums.OrderStatus;
import org.example.models.CartItem;
import org.example.models.Order;
import org.example.interfaces.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(int customerId, List<CartItem> cartItems, double totalAmount, String deliveryAddress) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(cartItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING.name());
        order.setDeliveryAddress(deliveryAddress);
        orderRepository.saveOrder(order);
        return order;
    }

    public List<Order> viewOrders(int customerId) {
        return orderRepository.getOrdersByCustomerId(customerId);
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.getOrderById(orderId);
        if (order != null) {
            order.setStatus(status.name());
            if (status == OrderStatus.DELIVERED) {
                order.setDeliveredAt(LocalDateTime.now());
            }
            orderRepository.updateOrder(order);
        } else {
            System.out.println("Order not found.");
        }
    }
}
