package org.example.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.Book;
import org.example.models.CartItem;
import org.example.services.BookServiceProxy;
import org.example.services.CartService;

public class BookUI {
    private static final String ERROR_LOADING_BOOKS = "Failed to load books.";
    private static final String ERROR_SEARCH_BOOKS = "Failed to search books.";
    private static final String ALERT_INVALID_QUANTITY = "Please enter a valid quantity.";

    private final BookServiceProxy bookServiceProxy = new BookServiceProxy();
    private final CartService cartService = new CartService();

    private final TableView<Book> bookTable = new TableView<>();
    private final TableView<CartItem> cartTable = new TableView<>();

    private final ObservableList<Book> bookData = FXCollections.observableArrayList();
    private final ObservableList<CartItem> cartData = FXCollections.observableArrayList();

    public VBox createMainLayout() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(15));

        Label titleLabel = createTitleLabel("Book Management System");
        HBox controls = createControls();
        configureTable(bookTable, 400);
        configureTable(cartTable, 200);

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

    private HBox createControls() {
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10, 0, 10, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title or author");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBooks(searchField.getText().trim()));

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> handleAddToCart());

        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(e -> viewCart());

        controls.getChildren().addAll(searchField, searchButton, addToCartButton, viewCartButton);
        return controls;
    }

    private void viewCart() {
        CartUI cartUI = new CartUI();
        Stage cartStage = new Stage();
        cartUI.start(cartStage);
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
            showAlert(Alert.AlertType.ERROR, "Error", ERROR_LOADING_BOOKS + " " + e.getMessage());
        }
    }

    private void searchBooks(String keyword) {
        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Search term cannot be empty.");
            return;
        }

        try {
            bookData.clear();
            bookData.addAll(bookServiceProxy.searchBooks(keyword));
            bookTable.setItems(bookData);

            if (bookData.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No books found for the search term: " + keyword);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", ERROR_SEARCH_BOOKS + " " + e.getMessage());
        }
    }

    private void handleAddToCart() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Book Selected", "Please select a book to add to cart.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText("Enter quantity for: " + selectedBook.getTitle());
        dialog.setContentText("Quantity:");

        dialog.showAndWait().ifPresent(quantityStr -> processAddToCart(selectedBook, quantityStr));
    }

    private void processAddToCart(Book selectedBook, String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);

            if (quantity <= 0 || quantity > selectedBook.getStock()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", ALERT_INVALID_QUANTITY);
                return;
            }

            cartService.addBookToCart(selectedBook.getId(), selectedBook.getTitle(), quantity, selectedBook.getPrice());
            selectedBook.setStock(selectedBook.getStock() - quantity);
            refreshUI();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
        }
    }

    private void refreshUI() {
        loadBooks();
    }

    private void loadCart() {
        cartData.clear();
        cartData.addAll(cartService.getCartItems());
        cartTable.setItems(cartData);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
