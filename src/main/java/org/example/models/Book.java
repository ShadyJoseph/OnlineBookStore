package org.example.models;

public class Book {
    private static int idCounter = 1; // Static counter for unique IDs
    private int id;
    private String title;
    private String author;
    private double price;
    private int stock;
    private String category;
    private int popularity;
    private String edition;
    private String coverImage;

    // Constructors
    public Book(String title, String author, double price, int stock, String category, int popularity, String edition, String coverImage) {
        this.id = idCounter++;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.popularity = popularity;
        this.edition = edition;
        this.coverImage = coverImage;
    }

    public Book(int id, String title, String author, double price, int stock, String category, int popularity, String edition, String coverImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.popularity = popularity;
        this.edition = edition;
        this.coverImage = coverImage;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public static void setIdCounter(int newCounter) {
        idCounter = newCounter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    // Override toString() for better representation of the book object
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", popularity=" + popularity +
                ", edition='" + edition + '\'' +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }
}
