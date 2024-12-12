package org.example.ui;

import org.example.models.CartItem;
import org.example.models.Order;
import org.example.services.CartService;
import org.example.services.OrderService;

import java.util.List;
import java.util.Scanner;

public class CartUI {
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final Scanner scanner = new Scanner(System.in);

    public void manageCart(int customerId) {
        while (true) {
            System.out.println("\nCart Management:");
            System.out.println("1. View Cart");
            System.out.println("2. Add Book to Cart");
            System.out.println("3. Edit Cart");
            System.out.println("4. Remove Book from Cart");
            System.out.println("5. Place Order");
            System.out.println("6. View Orders");
            System.out.println("7. Cancel Order");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> viewCart();
                case 2 -> addBookToCart();
                case 3 -> editCart();
                case 4 -> removeBookFromCart();
                case 5 -> placeOrder(customerId);
                case 6 -> viewOrders(customerId);
                case 7 -> cancelOrder();
                case 8 -> { return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewCart() {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Cart Items:");
            for (CartItem item : cartItems) {
                System.out.printf("Book ID: %d, Quantity: %d, Price: %.2f\n", item.getBookId(), item.getQuantity(), item.getPrice());
            }
        }
    }

    private void addBookToCart() {
        System.out.print("Enter Book ID: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();

        cartService.addToCart(new CartItem() {{
            setBookId(bookId);
            setQuantity(quantity);
            setPrice(price);
        }});
        System.out.println("Book added to cart.");
    }

    private void editCart() {
        System.out.print("Enter Book ID to edit: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter New Quantity: ");
        int newQuantity = scanner.nextInt();
        cartService.editCart(bookId, newQuantity);
        System.out.println("Cart updated.");
    }

    private void removeBookFromCart() {
        System.out.print("Enter Book ID to remove: ");
        int bookId = scanner.nextInt();
        cartService.removeFromCart(bookId);
        System.out.println("Book removed from cart.");
    }

    private void placeOrder(int customerId) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty. Add items to place an order.");
            return;
        }
        double totalAmount = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        Order order = orderService.placeOrder(customerId, cartItems, totalAmount);
        cartService.clearCart();
        System.out.println("Order placed successfully! Order ID: " + order.getId());
    }

    private void viewOrders(int customerId) {
        List<Order> orders = orderService.viewOrders(customerId);
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("Orders:");
            for (Order order : orders) {
                System.out.printf("Order ID: %d, Date: %s, Total: %.2f, Status: %s\n",
                        order.getId(), order.getOrderDate(), order.getTotalAmount(), order.getStatus());
            }
        }
    }

    private void cancelOrder() {
        System.out.print("Enter Order ID to cancel: ");
        int orderId = scanner.nextInt();
        orderService.cancelOrder(orderId);
        System.out.println("Order cancellation requested.");
    }
}
