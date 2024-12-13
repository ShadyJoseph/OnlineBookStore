package org.example.models;

public class Review {
    private int id;
    private int bookId;
    int customerId;
    private String reviewText;
    private int rating;

    // Default constructor
    public Review() {}

    // Constructor with parameters
    public Review(int bookId, int customerId, String reviewText, int rating) {
        this.bookId = bookId;
        this.customerId = customerId;
        this.reviewText = reviewText;
        setRating(rating); // Set rating using the setter to ensure it's valid
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public int getRating() { return rating; }
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    // Method to update review text and rating
    public void updateReview(String newReviewText, int newRating) {
        this.reviewText = newReviewText;
        setRating(newRating); // Ensure the new rating is valid
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", customerId=" + customerId +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                '}';
    }
}
