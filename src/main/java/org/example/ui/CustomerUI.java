package org.example.ui;

import org.example.models.Book;
import org.example.models.Category;
import org.example.models.Customer;
import org.example.services.AdminService;
import org.example.services.CustomerService;
import org.example.utils.ValidationUtil;

import java.util.Scanner;

public class CustomerUI {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService adminService = new AdminService();
    private final CustomerService customerService = new CustomerService();
    private Customer loggedInCustomer;

    public void start() {
        System.out.println("Welcome to the Online Book Store!");
        while (true) {
            System.out.println("\n1. Customer Menu\n2. Admin Menu\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> customerMenu();
                case 2 -> adminMenu();
                case 3 -> {
                    System.out.println("Thank you for visiting!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void customerMenu() {
        while (true) {
            System.out.println("\nCustomer Menu:");
            if (loggedInCustomer == null) {
                System.out.println("1. Sign Up\n2. Log In\n3. Back to Main Menu");
            } else {
                System.out.println("1. View Books\n2. Update Profile\n3. Log Out");
            }
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (loggedInCustomer == null) {
                switch (choice) {
                    case 1 -> signUp();
                    case 2 -> logIn();
                    case 3 -> {
                        System.out.println("Returning to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } else {
                switch (choice) {
                    case 1 -> viewBooks();
                    case 2 -> updateProfile();
                    case 3 -> {
                        loggedInCustomer = null;
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    private void signUp() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setAddress(address);
        customer.setPhone(phone);

        if (customerService.signUp(customer)) {
            System.out.println("Sign up successful!");
        } else {
            System.out.println("Username already exists. Try again.");
        }
    }

    private void logIn() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        Customer customer = customerService.login(username, password);
        if (customer != null) {
            loggedInCustomer = customer;
            System.out.println("Logged in successfully! Welcome, " + customer.getUsername() + ".");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void updateProfile() {
        if (loggedInCustomer == null) {
            System.out.println("You must be logged in to update your profile.");
            return;
        }

        System.out.print("Enter New Address: ");
        String newAddress = scanner.nextLine();
        System.out.print("Enter New Phone Number: ");
        String newPhone = scanner.nextLine();
        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        loggedInCustomer.setAddress(newAddress);
        loggedInCustomer.setPhone(newPhone);
        loggedInCustomer.setPassword(newPassword);

        if (customerService.updateCustomerDetails(loggedInCustomer)) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile. Try again.");
        }
    }

    private void viewBooks() {
        var books = adminService.listBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("Available Books:");
            books.forEach(book -> System.out.println(
                    "ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor()
            ));
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:\n1. Add Book\n2. Edit Book\n3. Delete Book\n4. Add Category\n5. List Books\n6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addBook();
                case 2 -> editBook();
                case 3 -> deleteBook();
                case 4 -> addCategory();
                case 5 -> listBooks();
                case 6 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addBook() {
        Book book = new Book();
        System.out.print("Enter Book ID: ");
        book.setId(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Title: ");
        book.setTitle(scanner.nextLine());
        System.out.print("Enter Author: ");
        book.setAuthor(scanner.nextLine());
        System.out.print("Enter Category: ");
        book.setCategory(scanner.nextLine());
        System.out.print("Enter Price: ");
        book.setPrice(scanner.nextDouble());
        System.out.print("Enter Stock: ");
        book.setStock(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        adminService.addBook(book);
    }

    private void editBook() {
        System.out.print("Enter Book ID to Edit: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Book updatedBook = new Book();
        System.out.print("Enter New Title: ");
        updatedBook.setTitle(scanner.nextLine());
        System.out.print("Enter New Author: ");
        updatedBook.setAuthor(scanner.nextLine());
        System.out.print("Enter New Category: ");
        updatedBook.setCategory(scanner.nextLine());
        System.out.print("Enter New Price: ");
        updatedBook.setPrice(scanner.nextDouble());
        System.out.print("Enter New Stock: ");
        updatedBook.setStock(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        adminService.editBook(bookId, updatedBook);
    }

    private void deleteBook() {
        System.out.print("Enter Book ID to Delete: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        adminService.deleteBook(bookId);
    }

    private void addCategory() {
        Category category = new Category();
        System.out.print("Enter Category ID: ");
        category.setId(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Category Name: ");
        category.setName(scanner.nextLine());
        adminService.addCategory(category);
    }

    private void listBooks() {
        var books = adminService.listBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.forEach(book -> System.out.println(
                    "ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor()
            ));
        }
    }
}
