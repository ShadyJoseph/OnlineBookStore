package org.example.services;

import org.example.models.Review;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {
    private static final String REVIEW_FILE = "src/main/resources/reviews.txt";

    public void addReview(int customerId, int bookId, String reviewText, int rating) {
        List<Review> reviews = readReviewsFromFile();
        Review review = new Review();
        review.setId(generateReviewId(reviews));
        review.setCustomerId(customerId);
        review.setBookId(bookId);
        review.setReviewText(reviewText);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        reviews.add(review);
        if (writeReviewsToFile(reviews)) {
            System.out.println("Review added successfully!");
        } else {
            System.out.println("Failed to save review to file.");
        }
    }

    public List<Review> getReviewsByCustomerId(int customerId) {
        List<Review> reviews = readReviewsFromFile();
        List<Review> customerReviews = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getCustomerId() == customerId) {
                customerReviews.add(review);
            }
        }
        return customerReviews;
    }

    public void updateReview(int reviewId, String reviewText, int rating) {
        List<Review> reviews = readReviewsFromFile();
        for (Review review : reviews) {
            if (review.getId() == reviewId) {
                review.setReviewText(reviewText);
                review.setRating(rating);
                review.setUpdatedAt(LocalDateTime.now());
                if (writeReviewsToFile(reviews)) {
                    System.out.println("Review updated successfully!");
                } else {
                    System.out.println("Failed to update review in file.");
                }
                return;
            }
        }
        System.out.println("Review not found!");
    }

    public void deleteReview(int reviewId) {
        List<Review> reviews = readReviewsFromFile();
        if (reviews.removeIf(review -> review.getId() == reviewId)) {
            if (writeReviewsToFile(reviews)) {
                System.out.println("Review deleted successfully!");
            } else {
                System.out.println("Failed to delete review from file.");
            }
        } else {
            System.out.println("Review not found!");
        }
    }

    private List<Review> readReviewsFromFile() {
        List<Review> reviews = new ArrayList<>();
        File file = new File(REVIEW_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error initializing reviews file: " + e.getMessage());
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEW_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Ensure all fields are present
                    Review review = new Review();
                    review.setId(Integer.parseInt(parts[0]));
                    review.setCustomerId(Integer.parseInt(parts[1]));
                    review.setBookId(Integer.parseInt(parts[2]));
                    review.setReviewText(parts[3]);
                    review.setRating(Integer.parseInt(parts[4]));
                    review.setCreatedAt(LocalDateTime.parse(parts[5]));
                    if (parts.length > 6) {
                        review.setUpdatedAt(LocalDateTime.parse(parts[6]));
                    }
                    reviews.add(review);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading reviews from file: " + e.getMessage());
        }
        return reviews;
    }

    private boolean writeReviewsToFile(List<Review> reviews) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE))) {
            for (Review review : reviews) {
                writer.write(review.getId() + "," +
                        review.getCustomerId() + "," +
                        review.getBookId() + "," +
                        review.getReviewText() + "," +
                        review.getRating() + "," +
                        review.getCreatedAt() +
                        (review.getUpdatedAt() != null ? "," + review.getUpdatedAt() : "") +
                        "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing reviews to file: " + e.getMessage());
            return false;
        }
    }

    private int generateReviewId(List<Review> reviews) {
        return reviews.stream().mapToInt(Review::getId).max().orElse(0) + 1;
    }
}
