package org.example.services;

import org.example.models.Book;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.Integer;
import java.lang.String;


public class BookService {
    private final List<Book> books = new ArrayList<>();
    private static final String FILE_PATH = "src/main/resources/books.txt";

    // Constructor: Load books from file
    public BookService() {
        loadBooksFromFile();
    }

    // Add a new book to the catalog (admin feature)
    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
        System.out.println("Book added: " + book);
    }

    // Remove a book by ID (admin feature)
    public boolean removeBookById(int bookId) {
        boolean removed = books.removeIf(book -> book.getId() == bookId);
        if (removed) {
            saveBooksToFile();
        }
        return removed;
    }

    // Update an existing book (admin feature)
    public boolean updateBook(Book updatedBook) {
        for (Book book : books) {
            if (book.getId() == updatedBook.getId()) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setPrice(updatedBook.getPrice());
                book.setStock(updatedBook.getStock());
                book.setCategory(updatedBook.getCategory());
                book.setPopularity(updatedBook.getPopularity());
                book.setEdition(updatedBook.getEdition());
                book.setCoverImage(updatedBook.getCoverImage());
                saveBooksToFile();
                System.out.println("Book updated: " + book);
                return true;
            }
        }
        System.out.println("Book not found for update.");
        return false;
    }

    // View all books (customer feature)
    public List<Book> viewAllBooks() {
        return new ArrayList<>(books);
    }

    // Search books by title or author (customer feature)
    public List<Book> searchBooks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                        book.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    // Filter books by category (customer feature)
    public List<Book> filterBooksByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // Sort books by popularity or price (customer feature)
    public List<Book> sortBooks(String sortBy) {
        Comparator<Book> comparator = switch (sortBy.toLowerCase()) {
            case "price" -> Comparator.comparingDouble(Book::getPrice);
            case "popularity" -> Comparator.comparingInt(Book::getPopularity).reversed();
            default -> throw new IllegalArgumentException("Invalid sort option: " + sortBy);
        };
        return books.stream().sorted(comparator).collect(Collectors.toList());
    }

    // Load books from file
    private void loadBooksFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Check if the line contains the expected number of fields
                if (parts.length == 9) { // Adjusted to match correct parts length
                    try {
                        // Trim each part to remove leading/trailing spaces
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String author = parts[2].trim();
                        double price = Double.parseDouble(parts[3].trim());
                        int stock = Integer.parseInt(parts[4].trim());
                        String category = parts[5].trim();
                        int popularity = Integer.parseInt(parts[6].trim());
                        String edition = parts[7].trim();
                        String coverImage = parts[8].trim();

                        // Create and add the book
                        Book book = new Book(title, author, price, stock, category, popularity, edition, coverImage);
                        books.add(book);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in line: " + line);
                    }
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
            System.out.println("Books loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading book file: " + e.getMessage());
        }
    }


    // Save books to file
    private void saveBooksToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Book book : books) {
                writer.write(String.format("%d,%s,%s,%.2f,%d,%s,%d,%s,%s%n",
                        book.getId(), book.getTitle(), book.getAuthor(),
                        book.getPrice(), book.getStock(), book.getCategory(),
                        book.getPopularity(), book.getEdition(), book.getCoverImage()));
            }
            System.out.println("Books saved successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to book file: " + e.getMessage());
        }
    }
}
