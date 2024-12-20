package org.example.services;

import org.example.models.Book;

import java.util.List;

public class BookServiceProxy extends BookService {
    private boolean isLoaded = false;

    @Override
    public List<Book> viewAllBooks() {
        reloadBooksFromFile();
        return super.viewAllBooks();
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        reloadBooksIfNeeded();
        return super.searchBooks(keyword);
    }

    @Override
    public void addBook(Book book) {
        reloadBooksIfNeeded();
        super.addBook(book);
    }

    private void reloadBooksIfNeeded() {
        if (!isLoaded) {
            reloadBooksFromFile();
        }
    }

    private void reloadBooksFromFile() {
        books.clear();
        books.addAll(fileManager.loadBooksFromFile());
        isLoaded = true;
    }
}