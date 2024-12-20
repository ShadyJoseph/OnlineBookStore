package org.example.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.models.Book;

public class BookUpdateDialog extends Dialog<Book> {

    public BookUpdateDialog(Book book) {
        setTitle("Update Book");
        setHeaderText("Update details for book: " + book.getTitle());

        // Create form fields (pre-populated)
        TextField titleField = createTextField(book.getTitle(), "Title");
        TextField authorField = createTextField(book.getAuthor(), "Author");
        TextField priceField = createTextField(String.valueOf(book.getPrice()), "Price");
        TextField stockField = createTextField(String.valueOf(book.getStock()), "Stock");
        TextField categoryField = createTextField(book.getCategory(), "Category");
        TextField popularityField = createTextField(String.valueOf(book.getPopularity()), "Popularity");
        TextField editionField = createTextField(book.getEdition(), "Edition");
        TextField coverImageField = createTextField(book.getCoverImage(), "Cover Image URL");

        // Add buttons
        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        // Layout
        VBox vbox = new VBox(10, titleField, authorField, priceField, stockField, categoryField, popularityField, editionField, coverImageField);
        vbox.setPrefWidth(400); // Optional: Set a preferred width for better alignment
        getDialogPane().setContent(vbox);

        // Result converter
        setResultConverter(dialogButton -> {
            if (dialogButton == updateButton) {
                return createBookFromForm(titleField, authorField, priceField, stockField, categoryField, popularityField, editionField, coverImageField);
            }
            return null;
        });
    }

    private TextField createTextField(String initialValue, String promptText) {
        TextField textField = new TextField(initialValue);
        textField.setPromptText(promptText);
        return textField;
    }

    private Book createBookFromForm(TextField titleField, TextField authorField, TextField priceField, TextField stockField,
                                    TextField categoryField, TextField popularityField, TextField editionField, TextField coverImageField) {
        try {
            double price = parseDouble(priceField.getText(), "Price");
            int stock = parseInt(stockField.getText(), "Stock");
            int popularity = parseInt(popularityField.getText(), "Popularity");

            return new Book(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    price,
                    stock,
                    categoryField.getText().trim(),
                    popularity,
                    editionField.getText().trim(),
                    coverImageField.getText().trim()
            );
        } catch (IllegalArgumentException e) {
            showError("Invalid Input", e.getMessage());
            return null;
        }
    }

    private double parseDouble(String text, String fieldName) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid decimal number.");
        }
    }

    private int parseInt(String text, String fieldName) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
