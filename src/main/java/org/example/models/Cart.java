package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items;

    // Default Constructor
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Add a CartItem to the cart
    public void addItem(CartItem item) {
        if (item == null) throw new IllegalArgumentException("Cart item cannot be null.");
        for (CartItem cartItem : items) {
            if (cartItem.equals(item)) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    // Remove a CartItem by bookId
    public void removeItem(int bookId) {
        items.removeIf(cartItem -> cartItem.getBookId() == bookId);
    }

    // Update quantity of an existing CartItem
    public void updateQuantity(int bookId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        for (CartItem cartItem : items) {
            if (cartItem.getBookId() == bookId) {
                cartItem.setQuantity(quantity);
                return;
            }
        }
        throw new IllegalArgumentException("Item with bookId " + bookId + " not found in cart.");
    }

    // Get total price of the cart
    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    // Clear the cart
    public void clear() {
        items.clear();
    }

    // Get the list of all CartItems
    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cart Contents:\n");
        for (CartItem item : items) {
            sb.append(item).append("\n");
        }
        sb.append("Total Price: ").append(getTotalPrice());
        return sb.toString();
    }
}
