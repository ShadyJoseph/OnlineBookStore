package org.example.models;

public class CartItem {
    private int bookId;
    private int quantity;
    private double price;

    // Default constructor
    public CartItem() {}

    // Constructor with parameters
    public CartItem(int bookId, int quantity, double price) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    // Method to calculate total price for this cart item (price * quantity)
    public double getTotalPrice() {
        return this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "bookId=" + bookId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem that = (CartItem) obj;
        return bookId == that.bookId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(bookId);
    }
}
