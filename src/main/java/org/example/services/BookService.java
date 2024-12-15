package org.example.services;

import org.example.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    protected final List<Book> books = new ArrayList<>();
    protected final FileManager fileManager = new FileManager();

    public void addBook(Book book) {
        books.add(book);
        fileManager.saveBooksToFile(books);
    }

    public boolean removeBookById(int bookId) {
        boolean removed = books.removeIf(book -> book.getId() == bookId);
        if (removed) {
            fileManager.saveBooksToFile(books);
        }
        return removed;
    }

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
                fileManager.saveBooksToFile(books);
                return true;
            }
        }
        return false;
    }

    public List<Book> viewAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> searchBooks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                        book.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}
