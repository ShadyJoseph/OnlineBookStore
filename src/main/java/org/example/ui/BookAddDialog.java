package org.example.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.models.Book;

public class BookAddDialog extends Dialog<Book> {
    public BookAddDialog() {
        setTitle("Add New Book");
        setHeaderText("Enter book details:");

        // Set up form fields
        TextField titleField = createTextField("Title");
        TextField authorField = createTextField("Author");
        TextField priceField = createTextField("Price");
        TextField stockField = createTextField("Stock");
        TextField categoryField = createTextField("Category");
        TextField popularityField = createTextField("Popularity");
        TextField editionField = createTextField("Edition");
        TextField coverImageField = createTextField("Cover Image");

        // Button for adding the book
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Add form fields to layout
        VBox vbox = new VBox(10, titleField, authorField, priceField, stockField, categoryField, popularityField, editionField, coverImageField);
        getDialogPane().setContent(vbox);

        // Set result converter to return book object
        setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return createBookFromForm(titleField, authorField, priceField, stockField, categoryField, popularityField, editionField, coverImageField);
            }
            return null;
        });
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    private Book createBookFromForm(TextField titleField, TextField authorField, TextField priceField, TextField stockField,
                                    TextField categoryField, TextField popularityField, TextField editionField, TextField coverImageField) {
        try {
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int popularity = Integer.parseInt(popularityField.getText());

            return new Book(
                    titleField.getText(),
                    authorField.getText(),
                    price,
                    stock,
                    categoryField.getText(),
                    popularity,
                    editionField.getText(),
                    coverImageField.getText()
            );
        } catch (NumberFormatException e) {
            showError("Invalid input", "Price, Stock, and Popularity must be valid numbers.");
            return null;
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
