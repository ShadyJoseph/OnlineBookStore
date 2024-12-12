package org.example.services;

import org.example.models.Book;
import org.example.models.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private static final String BOOK_FILE = "src/main/resources/books.txt";
    private final List<Category> categories = new ArrayList<>();

    public void addBook(Book book) {
        if (book != null) {
            List<Book> books = readBooksFromFile();
            book.setId(books.size() + 1); // Auto-increment ID
            books.add(book);
            if (writeBooksToFile(books)) {
                System.out.println("Book added successfully!");
            } else {
                System.err.println("Failed to save book to file.");
            }
        } else {
            System.err.println("Invalid book details!");
        }
    }

    public void editBook(int bookId, Book updatedBook) {
        List<Book> books = readBooksFromFile();
        for (Book book : books) {
            if (book.getId() == bookId) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setCategory(updatedBook.getCategory());
                book.setPrice(updatedBook.getPrice());
                book.setStock(updatedBook.getStock());
                book.setEdition(updatedBook.getEdition());
                book.setCoverImage(updatedBook.getCoverImage());
                if (writeBooksToFile(books)) {
                    System.out.println("Book updated successfully!");
                } else {
                    System.err.println("Failed to update book in file.");
                }
                return;
            }
        }
        System.err.println("Book not found!");
    }

    public void deleteBook(int bookId) {
        List<Book> books = readBooksFromFile();
        if (books.removeIf(book -> book.getId() == bookId)) {
            if (writeBooksToFile(books)) {
                System.out.println("Book deleted successfully!");
            } else {
                System.err.println("Failed to delete book from file.");
            }
        } else {
            System.err.println("Book not found!");
        }
    }

    public void addCategory(Category category) {
        if (category != null && !categories.contains(category)) {
            categories.add(category);
            System.out.println("Category added successfully!");
        } else {
            System.err.println("Invalid or duplicate category details!");
        }
    }

    public List<Book> listBooks() {
        return readBooksFromFile();
    }

    public List<Category> listCategories() {
        return new ArrayList<>(categories);
    }

    private List<Book> readBooksFromFile() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOK_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error initializing books file: " + e.getMessage());
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Book book = new Book();
                    book.setId(Integer.parseInt(parts[0]));
                    book.setTitle(parts[1]);
                    book.setAuthor(parts[2]);
                    book.setCategory(parts[3]);
                    book.setPrice(Double.parseDouble(parts[4]));
                    book.setStock(Integer.parseInt(parts[5]));
                    book.setEdition(parts[6]);
                    book.setCoverImage(parts.length > 7 ? parts[7] : null);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books from file: " + e.getMessage());
        }
        return books;
    }

    private boolean writeBooksToFile(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book book : books) {
                writer.write(book.getId() + "," +
                        book.getTitle() + "," +
                        book.getAuthor() + "," +
                        book.getCategory() + "," +
                        book.getPrice() + "," +
                        book.getStock() + "," +
                        book.getEdition() + "," +
                        (book.getCoverImage() != null ? book.getCoverImage() : "") + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing books to file: " + e.getMessage());
            return false;
        }
    }
}
