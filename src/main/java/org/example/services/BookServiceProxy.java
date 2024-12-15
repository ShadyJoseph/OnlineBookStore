package org.example.services;

import org.example.models.Book;
import java.util.List;

public class BookServiceProxy extends BookService {
    private boolean isLoaded = false;

    @Override
    public List<Book> viewAllBooks() {
        if (!isLoaded) {
            books.addAll(fileManager.loadBooksFromFile());
            isLoaded = true;
        }
        return super.viewAllBooks();
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        if (!isLoaded) {
            books.addAll(fileManager.loadBooksFromFile());
            isLoaded = true;
        }
        return super.searchBooks(keyword);
    }
}
