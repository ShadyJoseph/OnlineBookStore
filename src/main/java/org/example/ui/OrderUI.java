package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.services.OrderService;
import org.example.models.Order;

import java.text.NumberFormat;
import java.util.List;

public class OrderUI {
    private final OrderService orderService;
    private final ListView<String> orderListView = new ListView<>();
    private final Label totalAmountLabel = new Label("Total Amount: $0.00");
    private final int customerId;
    private final List<Order> orders;

    public OrderUI(int customerId, List<Order> orders) {
        this.orderService = new OrderService();
        this.customerId = customerId;
        this.orders = orders;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Order Management System");

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));

        Button cancelOrderButton = new Button("Cancel Order");
        Button trackOrderButton = new Button("Track Order");

        cancelOrderButton.setOnAction(e -> cancelOrder());
        trackOrderButton.setOnAction(e -> trackOrder());

        HBox buttonLayout = new HBox(15, cancelOrderButton, trackOrderButton);
        buttonLayout.setStyle("-fx-alignment: center;");

        orderListView.setPrefHeight(250);
        orderListView.setStyle("-fx-font-size: 14px;");

        mainLayout.getChildren().addAll(new Label("Your Orders:"), orderListView, totalAmountLabel, buttonLayout);

        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateOrderDisplay();
    }

    private void cancelOrder() {
        String selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Selection", "Please select an order to cancel.");
            return;
        }

        int orderId = extractOrderId(selectedOrder);

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Cancel Order");
        confirmationAlert.setHeaderText("Are you sure you want to cancel this order?");
        confirmationAlert.setContentText("Order ID: " + orderId);

        if (confirmationAlert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
            boolean success = orderService.cancelOrderByCustomer(orderId, customerId);
            if (success) {
                updateOrderDisplay();
                showAlert("Success", "Order has been cancelled.");
            } else {
                showAlert("Cancellation Failed", "Unable to cancel the order.");
            }
        }
    }

    private void trackOrder() {
        String selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("No Selection", "Please select an order to track.");
            return;
        }

        int orderId = extractOrderId(selectedOrder);
        String orderStatus = orderService.trackOrderStatus(orderId);

        if (orderStatus != null) {
            showAlert("Order Status", "Order ID: " + orderId + "\nStatus: " + orderStatus);
        } else {
            showAlert("Tracking Failed", "Unable to fetch the order status.");
        }
    }

    private void updateOrderDisplay() {
        orderListView.getItems().clear();

        double totalAmount = 0.0;
        for (Order order : orders) {
            String orderDisplay = String.format("Order ID: %d, Status: %s, Amount: $%.2f",
                    order.getId(), order.getStatus(), order.getTotalAmount());
            orderListView.getItems().add(orderDisplay);
            totalAmount += order.getTotalAmount();
        }

        String formattedAmount = NumberFormat.getCurrencyInstance().format(totalAmount);
        totalAmountLabel.setText("Total Amount: " + formattedAmount);
    }

    private int extractOrderId(String orderString) {
        String[] parts = orderString.split(",");
        for (String part : parts) {
            if (part.startsWith("Order ID:")) {
                return Integer.parseInt(part.split(":")[1].trim());
            }
        }
        throw new IllegalArgumentException("Invalid order string format.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
