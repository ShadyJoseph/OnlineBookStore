package org.example.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.services.UserService;

public class UserUI {

    private final Stage stage;
    private final UserService userService = new UserService();
    private int customerId;  // Store the customerId here

    public UserUI(Stage stage) {
        this.stage = stage;
    }

    // Main UI (Login and Signup)
    public void loadMainUI() {
        Label welcomeLabel = new Label("Welcome to the User Management System!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox signUpSection = createSignUpSection();
        VBox logInSection = createLogInSection();

        HBox mainLayout = new HBox(50, signUpSection, logInSection);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, welcomeLabel, mainLayout);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 700, 450);
        stage.setScene(scene);
        stage.setTitle("User Management System");
        stage.show();
    }

    // Login Section
    private VBox createLogInSection() {
        VBox layout = createSectionLayout("Log In");

        TextField loginUsernameField = createTextField("Username");
        PasswordField loginPasswordField = createPasswordField("Password");

        Button loginButton = new Button("Log In");
        loginButton.setOnAction(e -> handleLogin(loginUsernameField, loginPasswordField));

        layout.getChildren().addAll(loginUsernameField, loginPasswordField, loginButton);
        return layout;
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Username and Password are required.");
            return;
        }

        User user = userService.logIn(username, password);
        if (user != null) {
            this.customerId = user.getId();  // Store the customerId
            showAlert(Alert.AlertType.INFORMATION, "Success", "Login Successful!");
            loadBookUI();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials. Try again.");
        }
    }

    private void loadBookUI() {
        BookUI bookUI = new BookUI(customerId);  // Pass customerId to BookUI
        VBox bookLayout = bookUI.createMainLayout();
        Scene bookScene = new Scene(bookLayout, 800, 600);
        stage.setScene(bookScene);
        stage.setTitle("Book Management System");
    }

    // Sign-Up Section
    private VBox createSignUpSection() {
        VBox layout = createSectionLayout("Sign Up");

        TextField usernameField = createTextField("Username");
        PasswordField passwordField = createPasswordField("Password");
        PasswordField confirmPasswordField = createPasswordField("Confirm Password");
        TextField addressField = createTextField("Address");
        TextField phoneField = createTextField("Phone Number");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(e -> handleSignUp(usernameField, passwordField, confirmPasswordField, addressField, phoneField));

        layout.getChildren().addAll(usernameField, passwordField, confirmPasswordField, addressField, phoneField, signUpButton);
        return layout;
    }

    private void handleSignUp(TextField usernameField, PasswordField passwordField, PasswordField confirmPasswordField, TextField addressField, TextField phoneField) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Sign-Up Error", "All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match.");
            return;
        }

        User user = userService.signUp(username, password, address, phone);
        if (user != null) {
            this.customerId = user.getId();  // Store the customerId
            showAlert(Alert.AlertType.INFORMATION, "Success", "Sign-up successful!");
            clearFields(usernameField, passwordField, confirmPasswordField, addressField, phoneField);
        } else {
            showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Username already exists or invalid input.");
        }
    }

    private VBox createSectionLayout(String title) {
        Label label = new Label(title);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox layout = new VBox(10, label);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-color: gray; -fx-border-radius: 5px; -fx-border-width: 1px;");
        return layout;
    }

    private TextField createTextField(String placeholder) {
        TextField textField = new TextField();
        textField.setPromptText(placeholder);
        return textField;
    }

    private PasswordField createPasswordField(String placeholder) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(placeholder);
        return passwordField;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
}