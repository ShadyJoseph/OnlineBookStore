package org.example.services;

import org.example.models.Review;

import java.util.List;

public class ReviewService {

    // Method to fetch reviews for a specific book by its ID
    public List<Review> getReviewsForBook(int bookId) {
        return Review.getReviewsByBookId(bookId);
    }

    // Method to add a review to the database
    public void addReview(int bookId, int customerId, String reviewText, int rating) {
        // Validate the inputs
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Create a new review object
        Review review = new Review(bookId, customerId, reviewText, rating);

        // Add the review to the review database
        Review.addReview(review);
    }

    // Method to update an existing review by ID
    public boolean updateReview(int id, String newReviewText, int newRating) {
        // Validate the inputs
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Update the review
        return Review.updateReview(id, newReviewText, newRating);
    }

    // Method to fetch reviews by customerId
    public List<Review> getReviewsByCustomerId(int customerId) {
        return Review.viewReviewsByCustomerId(customerId);
    }
}
