package org.example.services;

import org.example.models.Book;
import org.example.models.Sale;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryService {
    private static final String SALES_FILE = "src/main/resources/sales.txt";
    protected final FileManager fileManager = new FileManager();

    private final List<Book> books;
    private final List<Sale> sales = new ArrayList<>();

    public InventoryService() {
        // Use FileManager to load books
        this.books = fileManager.loadBooksFromFile();
        loadSalesFromFile();
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

    // Methods to handle sales and books
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
        if (success) {
            // Delegate saving books to the FileManager
            fileManager.saveBooksToFile(books);
        }
        return success;
    }

    public boolean deleteBook(int bookId) {
        boolean success = books.removeIf(book -> book.getId() == bookId);
        if (success) {
            // Delegate saving books to the FileManager
            fileManager.saveBooksToFile(books);
        }
        return success;
    }
}
