package org.example.interfaces;

import org.example.models.Review;
import java.util.List;

public interface ReviewRepository {
    void saveReview(Review review);
    List<Review> getReviewsByCustomerId(int customerId);
    List<Review> getReviewsByBookId(int bookId);
    void updateReview(Review review);
    void deleteReview(int reviewId);
}
