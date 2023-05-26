package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.ReviewStorage;

import java.util.List;

@Slf4j
@Service
@Data
public class ReviewService {

    private ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public Review addReview(Review review) {
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    public long deleteReviewById(Long id) {
        return reviewStorage.deleteReviewById(id);
    }

    public Review findReviewById(Long id) {
        return reviewStorage.findReviewById(id);
    }

    public List<Review> findAllReviews(int filmId, int count) {
        return reviewStorage.findAllReviews(filmId, count);
    }

    public long likeReview(int userId, long id) {
        return reviewStorage.likeReview(userId, id);
    }

    public long dislikeReview(int userId, long id) {
        return reviewStorage.dislikeReview(userId, id);
    }

    public long deleteLikeReview(int userId, long id) {
        return reviewStorage.deleteLikeReview(userId, id);
    }

    public long deleteDislikeReview(int userId, long id) {
        return reviewStorage.deleteDislikeReview(userId, id);
    }
}