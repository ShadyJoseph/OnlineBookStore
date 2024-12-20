package org.example.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final String CART_FILE = "src/main/resources/cart.json";
    private final List<CartItem> items;

    // The single instance of the Cart
    private static Cart instance;

    // Private constructor to prevent instantiation
    private Cart() {
        this.items = new ArrayList<>();
        loadFromFile();
    }

    // Public method to get the instance
    public static Cart getInstance() {
        if (instance == null) {
            synchronized (Cart.class) {
                if (instance == null) {
                    instance = new Cart();
                }
            }
        }
        return instance;
    }

    public void addItem(CartItem item) {
        if (item == null) throw new IllegalArgumentException("Cart item cannot be null.");
        for (CartItem existingItem : items) {
            if (existingItem.getBookId() == item.getBookId()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                saveToFile();
                return;
            }
        }
        items.add(item);
        saveToFile();
    }

    public void removeItem(int bookId) {
        // Iterate over your cart items and remove the one matching the bookId
        items.removeIf(item -> item.getBookId() == bookId);  // Assuming `getBookId` is the method to retrieve the book ID
        saveToFile();  // Save the updated cart
    }

    public void updateQuantity(int bookId, int quantity) {
        for (CartItem item : items) {
            if (item.getBookId() == bookId) {
                item.setQuantity(quantity);
                saveToFile();
                return;
            }
        }
    }

    public void updatePrice(int bookId, double price) {
        for (CartItem item : items) {
            if (item.getBookId() == bookId) {
                item.setPrice(price);
                saveToFile();
                return;
            }
        }
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public void clear() {
        items.clear();
        saveToFile();
    }

    public List<CartItem> getItems() {
        return items;
    }

    private void saveToFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CART_FILE), items);
        } catch (IOException e) {
            System.err.println("Error saving cart to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(CART_FILE);
        if (file.exists()) {
            try {
                List<CartItem> loadedItems = mapper.readValue(file, new TypeReference<List<CartItem>>() {});
                items.addAll(loadedItems);
            } catch (IOException e) {
                System.err.println("Error loading cart from file: " + e.getMessage());
            }
        }
    }
}
