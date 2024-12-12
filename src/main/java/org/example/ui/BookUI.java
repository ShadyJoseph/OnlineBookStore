package org.example.ui;

import org.example.models.Book;
import org.example.services.BookService;

import java.util.List;
import java.util.Scanner;

public class BookUI {
    private final BookService bookService = new BookService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\nWelcome to the Book Browser!");
        while (true) {
            System.out.println("\n1. View All Books");
            System.out.println("2. Search Books by Title/Author");
            System.out.println("3. Filter Books by Category");
            System.out.println("4. Sort Books by Price or Popularity");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewAllBooks();
                case 2 -> searchBooks();
                case 3 -> filterBooks();
                case 4 -> sortBooks();
                case 5 -> {
                    System.out.println("Exiting Book Browser. Have a great day!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            displayBooks(books);
        }
    }

    private void searchBooks() {
        System.out.print("Enter a keyword to search by title or author: ");
        String keyword = scanner.nextLine();
        List<Book> results = bookService.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("No books found for the given keyword.");
        } else {
            displayBooks(results);
        }
    }

    private void filterBooks() {
        System.out.print("Enter a category to filter by (e.g., IT, History, Classics): ");
        String category = scanner.nextLine();
        List<Book> results = bookService.filterBooksByCategory(category);
        if (results.isEmpty()) {
            System.out.println("No books found in the given category.");
        } else {
            displayBooks(results);
        }
    }

    private void sortBooks() {
        System.out.print("Sort by (price/popularity): ");
        String sortBy = scanner.nextLine();
        System.out.print("Sort order (asc/desc): ");
        String sortOrder = scanner.nextLine();

        boolean ascending = sortOrder.equalsIgnoreCase("asc");
        List<Book> sortedBooks = bookService.sortBooks(sortBy, ascending);
        if (sortedBooks.isEmpty()) {
            System.out.println("No books available for sorting.");
        } else {
            displayBooks(sortedBooks);
        }
    }

    private void displayBooks(List<Book> books) {
        System.out.println("\nAvailable Books:");
        for (Book book : books) {
            System.out.printf("ID: %d, Title: %s, Author: %s, Price: $%.2f, Stock: %d, Category: %s, Popularity: %d\n",
                    book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getStock(), book.getCategory(), book.getPopularity());
        }
    }
}
