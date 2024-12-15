package org.example;

import org.example.services.BookServiceProxy;
import org.example.models.Book;

public class Main {
    public static void main(String[] args) {
        // Test book-related functionality
        testBookService();
    }

    private static void testBookService() {
        BookServiceProxy bookService = new BookServiceProxy();

        // Load and display books lazily
        System.out.println("All Books: " + bookService.viewAllBooks());

        // Add new books
        bookService.addBook(new Book("Java", "Shady", 50.0, 10, "Programming", 5, "3rd Edition", "image.jpg"));
        bookService.addBook(new Book("C++", "Youssef", 60.0, 10, "Programming", 5, "3rd Edition", "image.jpg"));
        bookService.addBook(new Book("JavaScript", "Afsha", 70.0, 10, "Programming", 5, "3rd Edition", "image.jpg"));

        // Search books
        System.out.println("Search Results: " + bookService.searchBooks("java"));
    }
}
