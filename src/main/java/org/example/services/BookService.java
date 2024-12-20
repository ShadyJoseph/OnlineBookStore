package org.example.services;

import javafx.scene.control.Alert;
import org.example.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BookService {
    protected final List<Book> books = new ArrayList<>();
    protected final FileManager fileManager = new FileManager();

    private void reloadBooksFromFile() {
        // Clear the current list and reload the books from the file
        books.clear();
        books.addAll(fileManager.loadBooksFromFile());
    }

    public void addBook(Book book) {
        // Reload the books list from the file before adding a new book
        reloadBooksFromFile();

        // Add the new book to the list
        books.add(book);

        // Save the updated list of books to the file
        fileManager.saveBooksToFile(books);
    }

    public boolean removeBookById(int bookId) {
        // Reload the books list from the file before removing a book
        reloadBooksFromFile();

        // Remove the book with the specified ID
        boolean removed = books.removeIf(book -> book.getId() == bookId);

        if (removed) {
            // Save the updated list of books to the file
            fileManager.saveBooksToFile(books);
        }
        return removed;
    }

    public boolean updateBook(Book updatedBook) {
        // Ensure the book list is loaded (this could be optimized to avoid unnecessary reloads)
        reloadBooksFromFile();

        // Iterate through the books list to find the matching book by ID
        for (Book book : books) {
            if (book.getId() == updatedBook.getId()) {
                // Update the book details
                book.updateDetails(updatedBook);

                // Save the updated list of books to the file
                try {
                    fileManager.saveBooksToFile(books);
                } catch (Exception e) {
                    // Handle file save errors if any
                    return false;
                }

                return true; // Return true if the book is updated successfully
            }
        }
        // Return false if no book with the given ID was found
        return false;
    }


    public List<Book> viewAllBooks() {
        // Reload the books list from the file before viewing
        reloadBooksFromFile();
        return new ArrayList<>(books);
    }

    public List<Book> searchBooks(String keyword) {
        // Reload the books list from the file before searching
        reloadBooksFromFile();

        // Perform a case-insensitive search for books by title or author
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                        book.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}
