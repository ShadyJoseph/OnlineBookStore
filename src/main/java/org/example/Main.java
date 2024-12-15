package org.example;

import org.example.models.Book;
import org.example.services.BookServiceProxy;

public class Main {
    public static void main(String[] args) {
        BookServiceProxy bookService = new BookServiceProxy();

        // Load and display books lazily
        System.out.println("All Books: " + bookService.viewAllBooks());

        // Add a new book
        bookService.addBook(new Book( "Java", "Shady", 50.0, 10, "Programming", 5, "3rd Edition", "image.jpg"));
        bookService.addBook(new Book( "C++", "Youssef", 60.0, 10, "Programming", 5, "3rd Edition", "image.jpg"));
        bookService.addBook(new Book( "JavaScript", "afsha", 70.0, 10, "Programming", 5, "3rd Edition", "image.jpg"));
        // Search books
        System.out.println("Search Results: " + bookService.searchBooks("java"));
    }
}
