package org.example.services;

import org.example.models.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private static final String BOOK_FILE = "src/main/resources/books.txt";

    // Retrieve all books
    public List<Book> getAllBooks() {
        return readBooksFromFile();
    }

    // Search books by title or author
    public List<Book> searchBooks(String keyword) {
        List<Book> books = readBooksFromFile();
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    // Filter books by category
    public List<Book> filterBooksByCategory(String category) {
        List<Book> books = readBooksFromFile();
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                results.add(book);
            }
        }
        return results;
    }

    // Sort books by popularity or price
    public List<Book> sortBooks(String sortBy, boolean ascending) {
        List<Book> books = readBooksFromFile();
        books.sort((b1, b2) -> {
            int comparison = 0;
            if ("price".equalsIgnoreCase(sortBy)) {
                comparison = Double.compare(b1.getPrice(), b2.getPrice());
            } else if ("popularity".equalsIgnoreCase(sortBy)) {
                comparison = Integer.compare(b1.getPopularity(), b2.getPopularity());
            }
            return ascending ? comparison : -comparison;
        });
        return books;
    }

    // Helper method: Read books from file
    private List<Book> readBooksFromFile() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Book book = new Book();
                    book.setId(Integer.parseInt(parts[0]));
                    book.setTitle(parts[1]);
                    book.setAuthor(parts[2]);
                    book.setPrice(Double.parseDouble(parts[3]));
                    book.setStock(Integer.parseInt(parts[4]));
                    book.setCategory(parts[5]);
                    book.setPopularity(Integer.parseInt(parts[6]));
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books from file: " + e.getMessage());
        }
        return books;
    }
}
