package org.example.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.Book;
import org.example.services.BookService;
import org.example.services.BookServiceProxy;
import org.example.services.OrderService;

import java.util.List;

public class AdminBookUI {

    private static final String ERROR_LOADING_BOOKS = "Failed to load books.";
    private static final String ERROR_SEARCH_BOOKS = "Failed to search books.";
    private static final String ALERT_INVALID_QUANTITY = "Please enter a valid quantity.";

    private final BookServiceProxy bookServiceProxy = new BookServiceProxy();
    private final BookService bookService = new BookService();

    private final TableView<Book> bookTable = new TableView<>();
    private final ObservableList<Book> bookData = FXCollections.observableArrayList();

    // Constructor
    public AdminBookUI() {}

    public VBox createMainLayout() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(15));

        Label titleLabel = createTitleLabel("Admin - Book Management System");
        HBox controls = createAdminControls();
        configureTable(bookTable, 400);

        loadBookTableColumns();
        loadBooks();

        mainLayout.getChildren().addAll(titleLabel, controls, bookTable);
        return mainLayout;
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        return label;
    }

    private HBox createAdminControls() {
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10, 0, 10, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title or author");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBooks(searchField.getText().trim()));

        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> addBook());

        Button deleteButton = new Button("Delete Book");
        deleteButton.setOnAction(e -> deleteBook());

        Button updateButton = new Button("Update Book");
        updateButton.setOnAction(e -> updateBook());

        // New button to view orders
        Button viewOrdersButton = new Button("View Orders");
        viewOrdersButton.setOnAction(e -> viewOrders());

        controls.getChildren().addAll(searchField, searchButton, addButton, deleteButton, updateButton, viewOrdersButton);
        return controls;
    }

    private void addBook() {
        BookAddDialog dialog = new BookAddDialog();
        dialog.showAndWait().ifPresent(book -> {
            try {
                validateBookDetails(book);
                bookService.addBook(book);
                refreshUI();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add book: " + e.getMessage());
            }
        });
    }

    private void viewOrders() {
        // Initialize OrderService for loading orders
        OrderService orderService = new OrderService();

        // Create AdminOrderUI instance to manage and display admin orders
        AdminOrderUI adminOrderUI = new AdminOrderUI(orderService);

        // Create a new Stage to display the orders
        Stage orderStage = new Stage();
        adminOrderUI.start(orderStage);
    }


    private void deleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to delete.");
            return;
        }

        try {
            boolean deleted = bookService.removeBookById(selectedBook.getId());
            if (deleted) {
                refreshUI();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the book.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting book: " + e.getMessage());
        }
    }

    private void updateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to update.");
            return;
        }

        BookUpdateDialog dialog = new BookUpdateDialog(selectedBook);
        dialog.showAndWait().ifPresent(updatedBook -> {
            try {
                validateBookDetails(updatedBook);
                bookService.updateBook(updatedBook);
                refreshUI();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book updated successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update book: " + e.getMessage());
            }
        });
    }

    private void validateBookDetails(Book book) throws IllegalArgumentException {
        if (book.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (book.getStock() < 0) {
            throw new IllegalArgumentException("Stock must be a positive integer.");
        }
    }

    private void configureTable(TableView<?> table, double height) {
        table.setPrefHeight(height);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadBookTableColumns() {
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getId()));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(param -> param.getValue().titleProperty());

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(param -> param.getValue().authorProperty());

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getStock()));

        bookTable.getColumns().addAll(idColumn, titleColumn, authorColumn, priceColumn, stockColumn);
    }

    private void loadBooks() {
        try {
            bookData.clear();
            bookData.addAll(bookServiceProxy.viewAllBooks());
            bookTable.setItems(bookData);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", ERROR_LOADING_BOOKS);
        }
    }

    private void searchBooks(String keyword) {
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }

        try {
            List<Book> result = bookServiceProxy.searchBooks(keyword);
            if (result.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No books found for the given search.");
            } else {
                bookData.setAll(result);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", ERROR_SEARCH_BOOKS);
        }
    }

    private void refreshUI() {
        loadBooks();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
