package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.services.CartService;

public class CartUI {
    private final CartService cartService;
    private final ListView<String> cartListView = new ListView<>();
    private final Label totalPriceLabel = new Label("Total Price: $0.0");

    public CartUI() {
        this.cartService = new CartService();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cart Management System");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));

        Button removeItemButton = new Button("Remove Item");
        Button updateItemButton = new Button("Update Quantity");
        Button clearCartButton = new Button("Clear Cart");

        removeItemButton.setOnAction(e -> removeItem());
        updateItemButton.setOnAction(e -> updateQuantity());
        clearCartButton.setOnAction(e -> clearCart());

        HBox buttonLayout = new HBox(10, removeItemButton, updateItemButton, clearCartButton);

        cartListView.setPrefHeight(200);

        mainLayout.getChildren().addAll(new Label("Your Cart:"), cartListView, totalPriceLabel, buttonLayout);

        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateCartDisplay();
    }

    private void removeItem() {
        String selectedItem = cartListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to remove.");
            return;
        }

        int bookId = extractBookId(selectedItem);

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Remove Item");
        confirmationAlert.setHeaderText("Are you sure you want to remove this item?");
        confirmationAlert.setContentText(selectedItem);

        if (confirmationAlert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
            cartService.removeBookFromCart(bookId);
            updateCartDisplay();
        }
    }

    private void updateQuantity() {
        String selectedItem = cartListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to update.");
            return;
        }

        int bookId = extractBookId(selectedItem);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Quantity");
        dialog.setHeaderText("Enter New Quantity");
        dialog.setContentText("Quantity:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int newQuantity = Integer.parseInt(input);
                if (newQuantity <= 0) {
                    showAlert("Invalid Quantity", "Quantity must be greater than zero.");
                } else {
                    cartService.updateQuantity(bookId, newQuantity);
                    updateCartDisplay();
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number.");
            }
        });
    }

    private void clearCart() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Clear Cart");
        confirmationAlert.setHeaderText("Are you sure you want to clear the cart?");
        confirmationAlert.setContentText("This action cannot be undone.");

        if (confirmationAlert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
            cartService.clearCart();
            updateCartDisplay();
        }
    }

    private void updateCartDisplay() {
        cartListView.getItems().clear();
        cartService.getCartItems().forEach(item -> cartListView.getItems().add(item.toString()));
        totalPriceLabel.setText("Total Price: $" + cartService.calculateTotal());
    }

    private int extractBookId(String cartItemString) {
        String[] parts = cartItemString.split(",");
        for (String part : parts) {
            if (part.startsWith("bookId=")) {
                return Integer.parseInt(part.split("=")[1]);
            }
        }
        return -1;  // Return an invalid book ID if not found
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
