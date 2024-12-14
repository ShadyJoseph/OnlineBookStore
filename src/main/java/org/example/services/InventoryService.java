package org.example.services;

import org.example.models.Book;
import org.example.models.Sale;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InventoryService {
    private static final String BOOKS_FILE = "src/main/resources/books.txt";
    private static final String SALES_FILE = "src/main/resources/sales.txt";

    private final List<Book> books = new ArrayList<>();
    private final List<Sale> sales = new ArrayList<>();

    public InventoryService() {
        loadBooksFromFile();
        loadSalesFromFile();
    }

    // Load books from file
    private void loadBooksFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String author = parts[2].trim();
                        double price = Double.parseDouble(parts[3].trim());
                        int stock = Integer.parseInt(parts[4].trim());
                        String category = parts[5].trim();
                        int popularity = Integer.parseInt(parts[6].trim());
                        String edition = parts[7].trim();
                        String coverImage = parts[8].trim();

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

    // Load sales from file
    private void loadSalesFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Sale sale = new Sale(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            Integer.parseInt(parts[2]),
                            Double.parseDouble(parts[3])
                    );
                    sales.add(sale);
                }
            }
            System.out.println("Sales loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading sales file: " + e.getMessage());
        }
    }

    // Save books to file
    private void saveBooksToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(String.format("%d,%s,%s,%.2f,%d,%s,%d,%s,%s%n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getStock(),
                        book.getCategory(),
                        book.getPopularity(),
                        book.getEdition(),
                        book.getCoverImage()));
            }
            System.out.println("Books saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving books file: " + e.getMessage());
        }
    }

    // Save sales to file
    private void saveSalesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SALES_FILE))) {
            for (Sale sale : sales) {
                writer.write(String.format("%d,%s,%d,%.2f%n",
                        sale.getBookId(),
                        sale.getBookTitle(),
                        sale.getQuantitySold(),
                        sale.getTotalRevenue()));
            }
            System.out.println("Sales saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving sales file: " + e.getMessage());
        }
    }

    // Methods to handle sales and books (without @Override)
    public boolean recordSale(int bookId, int quantitySold) {
        boolean success = false;
        // Logic to record sale (ensure the sale is valid)
        Optional<Book> book = books.stream().filter(b -> b.getId() == bookId).findFirst();
        if (book.isPresent()) {
            Book b = book.get();
            if (b.getStock() >= quantitySold) {
                b.setStock(b.getStock() - quantitySold);
                double totalRevenue = b.getPrice() * quantitySold;
                Sale sale = new Sale(bookId, b.getTitle(), quantitySold, totalRevenue);
                sales.add(sale);
                success = true;
            }
        }
        if (success) saveSalesToFile();
        return success;
    }

    public boolean addNewBook(Book book) {
        boolean success = books.add(book);
        if (success) saveBooksToFile();
        return success;
    }

    public boolean deleteBook(int bookId) {
        boolean success = books.removeIf(book -> book.getId() == bookId);
        if (success) saveBooksToFile();
        return success;
    }
}
