package org.example.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final String CART_FILE = "src/main/resources/cart.txt";
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
        items.removeIf(item -> item.getBookId() == bookId);
        saveToFile();
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CART_FILE))) {
            oos.writeObject(new ArrayList<>(items)); // Use a copy to avoid serialization issues
        } catch (IOException e) {
            System.err.println("Error saving cart to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(CART_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<CartItem> loadedItems = (List<CartItem>) ois.readObject();
                items.addAll(loadedItems);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading cart from file: " + e.getMessage());
            }
        }
    }
}
