package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.services.UserService;
import org.example.models.User;

public class UserInterface extends Application {
    private final UserService userService = new UserService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Management System");

        // Welcome Section
        Label welcomeLabel = new Label("Welcome to the User Management System!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Sign-Up Section
        Label signUpLabel = new Label("Sign Up");
        signUpLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        Button signUpButton = new Button("Sign Up");

        signUpButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "All fields are required for sign-up!");
                return;
            }

            boolean success = userService.signUp(username, password, address, phone);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sign-up successful!");
                clearFields(usernameField, passwordField, addressField, phoneField);
            } else {
                showAlert(Alert.AlertType.ERROR, "Sign-up failed. Username might already exist.");
            }
        });

        // Login Section
        Label logInLabel = new Label("Log In");
        logInLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username");
        PasswordField loginPasswordField = new PasswordField();
        loginPasswordField.setPromptText("Password");
        Button loginButton = new Button("Log In");

        loginButton.setOnAction(e -> {
            String username = loginUsernameField.getText().trim();
            String password = loginPasswordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Username and Password are required for login!");
                return;
            }

            User user = userService.logIn(username, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Welcome back, " + user.getUsername() + "!");
                clearFields(loginUsernameField, loginPasswordField);
                welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login failed. Invalid credentials.");
            }
        });

        // Layout
        VBox signUpSection = new VBox(10, signUpLabel, usernameField, passwordField, addressField, phoneField, signUpButton);
        VBox logInSection = new VBox(10, logInLabel, loginUsernameField, loginPasswordField, loginButton);

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20));
        layout.setVgap(15);
        layout.setHgap(20);
        layout.add(welcomeLabel, 0, 0, 2, 1);
        layout.add(signUpSection, 0, 1);
        layout.add(logInSection, 1, 1);

        // Scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.show();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
