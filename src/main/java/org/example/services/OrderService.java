package org.example.services;

import org.example.enums.OrderStatus;
import org.example.models.CartItem;
import org.example.models.Order;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    private static final String ORDER_FILE = "src/main/resources/orders.txt";
    private int orderCounter = 1;

    public OrderService() {
        loadOrders();
    }

    // Place an order
    public Order placeOrder(int customerId, List<CartItem> cartItems, double totalAmount) {
        Order order = new Order();
        order.setId(orderCounter++);
        order.setCustomerId(customerId);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(cartItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING.name());
        orders.add(order);
        saveOrders();
        return order;
    }

    // View orders by customer ID
    public List<Order> viewOrders(int customerId) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomerId() == customerId) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    // Cancel an order by ID
    public void cancelOrder(int orderId) {
        for (Order order : orders) {
            if (order.getId() == orderId && OrderStatus.PENDING.name().equals(order.getStatus())) {
                order.setStatus(OrderStatus.CANCELED.name());
                saveOrders();
                break;
            }
        }
    }

    // Save orders to file
    private void saveOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : orders) {
                writer.write(order.getId() + "," + order.getCustomerId() + "," +
                        order.getOrderDate() + "," + order.getTotalAmount() + "," + order.getStatus());
                writer.newLine();
                for (CartItem item : order.getItems()) {
                    writer.write(item.getBookId() + "," + item.getQuantity() + "," + item.getPrice());
                    writer.newLine();
                }
                writer.write("END"); // Marker for the end of an order
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    // Load orders from file
    private void loadOrders() {
        orders.clear();
        File file = new File(ORDER_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
                String line;
                Order order = null;
                List<CartItem> cartItems = null;

                while ((line = reader.readLine()) != null) {
                    if (line.equals("END")) {
                        if (order != null) {
                            order.setItems(cartItems);
                            orders.add(order);
                        }
                        order = null;
                        cartItems = null;
                    } else if (order == null) {
                        String[] parts = line.split(",");
                        order = new Order();
                        order.setId(Integer.parseInt(parts[0]));
                        order.setCustomerId(Integer.parseInt(parts[1]));
                        order.setOrderDate(LocalDateTime.parse(parts[2]));
                        order.setTotalAmount(Double.parseDouble(parts[3]));
                        order.setStatus(parts[4]);
                        cartItems = new ArrayList<>();
                    } else {
                        String[] parts = line.split(",");
                        CartItem item = new CartItem();
                        item.setBookId(Integer.parseInt(parts[0]));
                        item.setQuantity(Integer.parseInt(parts[1]));
                        item.setPrice(Double.parseDouble(parts[2]));
                        cartItems.add(item);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading orders: " + e.getMessage());
            }
        }
    }
}
