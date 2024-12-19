package org.example.services;

import org.example.models.Cart;
import org.example.models.CartItem;

import java.util.List;

public class CartService {
    private final Cart cart;

    public CartService() {
        this.cart = new Cart(); // Instantiate the cart object
    }

    public void addBookToCart(int bookId, String bookName, int quantity, double price) {
        cart.addItem(new CartItem(bookId, bookName, quantity, price));
    }

    public void removeBookFromCart(int bookId) {
        cart.removeItem(bookId);
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