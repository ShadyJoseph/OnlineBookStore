package org.example.services;

import org.example.models.CartItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final List<CartItem> cart = new ArrayList<>();
    private static final String CART_FILE = "src/main/resources/cart.txt";

    public CartService() {
        loadCart();
    }

    // Add a book to the cart
    public void addToCart(CartItem item) {
        for (CartItem cartItem : cart) {
            if (cartItem.getBookId() == item.getBookId()) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                saveCart();
                return;
            }
        }
        cart.add(item);
        saveCart();
    }

    // Edit the quantity of a book in the cart
    public void editCart(int bookId, int newQuantity) {
        for (CartItem cartItem : cart) {
            if (cartItem.getBookId() == bookId) {
                cartItem.setQuantity(newQuantity);
                saveCart();
                return;
            }
        }
    }

    // Remove a book from the cart
    public void removeFromCart(int bookId) {
        cart.removeIf(cartItem -> cartItem.getBookId() == bookId);
        saveCart();
    }

    // Get all items in the cart
    public List<CartItem> getCartItems() {
        return cart;
    }

    // Clear the cart
    public void clearCart() {
        cart.clear();
        saveCart();
    }

    // Save cart to file
    private void saveCart() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE))) {
            for (CartItem item : cart) {
                writer.write(item.getBookId() + "," + item.getQuantity() + "," + item.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }

    // Load cart from file
    private void loadCart() {
        cart.clear();
        File file = new File(CART_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    CartItem item = new CartItem();
                    item.setBookId(Integer.parseInt(parts[0]));
                    item.setQuantity(Integer.parseInt(parts[1]));
                    item.setPrice(Double.parseDouble(parts[2]));
                    cart.add(item);
                }
            } catch (IOException e) {
                System.err.println("Error loading cart: " + e.getMessage());
            }
        }
    }
}
