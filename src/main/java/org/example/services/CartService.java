package org.example.services;

import org.example.models.Cart;
import org.example.models.CartItem;

import java.util.List;

public class CartService {
    private final Cart cart;

    public CartService() {
        this.cart = Cart.getInstance(); // Use Singleton to get the Cart instance
    }

    public void addBookToCart(int customerId, int bookId, String bookName, int quantity, double price) {
        CartItem newItem = new CartItem(bookId, bookName, quantity, price);
        cart.addItem(newItem);
    }

    public void removeBookFromCart(int bookId) {
        cart.removeItem(bookId); // Call your remove logic with the bookId
    }

    public void updateQuantity(int bookId, int quantity) {
        cart.updateQuantity(bookId, quantity);
    }

    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    public void clearCart() {
        cart.clear();
    }

    public double calculateTotal() {
        return cart.getTotalPrice();
    }
}
