package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.ReviewStorage;

import java.util.List;

@Slf4j
@Service
@Data
public class ReviewService {

    private ReviewStorage reviewStorage;
    private EventService eventService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, EventService eventService) {
        this.reviewStorage = reviewStorage;
        this.eventService = eventService;
    }

    public Review addReview(Review review) {
        Review reviewNew = reviewStorage.addReview(review);
        eventService.createEvent(reviewNew.getUserId(),Math.toIntExact(reviewNew.getReviewId()), EventType.REVIEW,
                EventOperation.ADD);
        return reviewNew;
    }

    public Review updateReview(Review review) {
        Review review1 = reviewStorage.updateReview(review);
        eventService.createEvent(review1.getUserId(), Math.toIntExact(review1.getReviewId()),EventType.REVIEW,
                EventOperation.UPDATE);
        return review1;
    }
//findReviewById(review.getReviewId()).getUserId()
    public long deleteReviewById(Long id) {
       int userId = findReviewById(id).getUserId();
       eventService.createEvent(userId, Math.toIntExact(id), EventType.REVIEW, EventOperation.REMOVE);
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