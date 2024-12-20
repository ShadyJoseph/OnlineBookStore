package org.example.services;

import org.example.models.Book;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_PATH = "src/main/resources/books.txt";

    // Loads books from the file and returns a list of books
    public List<Book> loadBooksFromFile() {
        List<Book> books = new ArrayList<>();
        int maxId = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
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

                        books.add(new Book(id, title, author, price, stock, category, popularity, edition, coverImage));
                        maxId = Math.max(maxId, id);
                    } catch (NumberFormatException e) {
                        // Log error and continue processing next line
                        System.err.println("Invalid number format for line: " + line);
                    }
                } else {
                    // Invalid format, log this line and continue
                    System.err.println("Invalid book format (should have 9 fields): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }

        // Ensure the ID counter is set to the next available ID
        Book.setIdCounter(maxId + 1);
        return books;
    }

    // Saves the current list of books to the file
    public void saveBooksToFile(List<Book> books) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Book book : books) {
                writer.write(String.format("%d,%s,%s,%.2f,%d,%s,%d,%s,%s%n",
                        book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(),
                        book.getStock(), book.getCategory(), book.getPopularity(),
                        book.getEdition(), book.getCoverImage()));
            }
        } catch (IOException e) {
            System.err.println("Error writing books to file: " + e.getMessage());
        }
    }
}
