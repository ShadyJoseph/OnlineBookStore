package org.example.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import org.example.models.Book;
import org.example.services.BookServiceProxy;

public class BookUI {
    private final BookServiceProxy bookServiceProxy = new BookServiceProxy();
    private final TableView<Book> bookTable = new TableView<>();
    private final ObservableList<Book> bookData = FXCollections.observableArrayList();

    public VBox createMainLayout() {
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        Label titleLabel = new Label("Book Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox controls = createControls();
        bookTable.setPrefHeight(400);

        loadTableColumns();
        loadBooks();

        mainLayout.getChildren().addAll(titleLabel, controls, bookTable);
        return mainLayout;
    }

    private HBox createControls() {
        HBox controls = new HBox(10);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title or author");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBooks(searchField.getText()));

        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> showAddBookDialog());

        Button removeButton = new Button("Remove Book");
        removeButton.setOnAction(e -> removeSelectedBook());

        Button updateButton = new Button("Update Book");
        updateButton.setOnAction(e -> showUpdateBookDialog());

        controls.getChildren().addAll(searchField, searchButton, addButton, updateButton, removeButton);
        return controls;
    }

    private void loadTableColumns() {
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());

        bookTable.getColumns().addAll(idColumn, titleColumn, authorColumn, priceColumn, stockColumn);
    }
    private void loadBooks() {
        bookData.clear();
        bookData.addAll(bookServiceProxy.viewAllBooks());
        bookTable.setItems(bookData);
    }

    private void searchBooks(String keyword) {
        bookData.clear();
        bookData.addAll(bookServiceProxy.searchBooks(keyword));
        bookTable.setItems(bookData);
    }

    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add Book");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField stockField = new TextField();
        stockField.setPromptText("Stock");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(stockField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return new Book(titleField.getText(), authorField.getText(),
                        Double.parseDouble(priceField.getText()), Integer.parseInt(stockField.getText()),
                        "General", 0, "1st Edition", "");
            }
            return null;
        });

        dialog.showAndWait().ifPresent(book -> {
            bookServiceProxy.addBook(book);
            loadBooks();
        });
    }

    private void removeSelectedBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookServiceProxy.removeBookById(selectedBook.getId());
            loadBooks();
        } else {
            showAlert("No Book Selected", "Please select a book to remove.", Alert.AlertType.WARNING);
        }
    }

    private void showUpdateBookDialog() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Similar to showAddBookDialog, pre-fill fields with `selectedBook` values
        } else {
            showAlert("No Book Selected", "Please select a book to update.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
