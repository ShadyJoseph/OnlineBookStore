package org.example.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.models.Book;

public class BookUpdateDialog extends Dialog<Book> {
    public BookUpdateDialog(Book book) {
        setTitle("Update Book");
        setHeaderText("Update details for book: " + book.getTitle());

        // Set up form fields (pre-populated with book details)
        TextField titleField = createTextField(book.getTitle(), "Title");
        TextField authorField = createTextField(book.getAuthor(), "Author");
        TextField priceField = createTextField(String.valueOf(book.getPrice()), "Price");
        TextField stockField = createTextField(String.valueOf(book.getStock()), "Stock");
        TextField categoryField = createTextField(book.getCategory(), "Category");
        TextField popularityField = createTextField(String.valueOf(book.getPopularity()), "Popularity");
        TextField editionField = createTextField(book.getEdition(), "Edition");
        TextField coverImageField = createTextField(book.getCoverImage(), "Cover Image");

        // Button for updating the book
        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        // Add form fields to layout
        VBox vbox = new VBox(10, titleField, authorField, priceField, stockField, categoryField, popularityField, editionField, coverImageField);
        getDialogPane().setContent(vbox);

        // Set result converter to return updated book object
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
