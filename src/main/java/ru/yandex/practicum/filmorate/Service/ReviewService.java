package ru.yandex.practicum.filmorate.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ErrorsIO.MethodArgumentNotException;
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
       eventService.createEvent(reviewNew.getUserId(),
               Math.toIntExact(reviewNew.getReviewId()),
               EventType.REVIEW,
               EventOperation.ADD);
        return reviewNew;
    }

    public Review updateReview(Review review) {
        Review reviewUpdated = reviewStorage.updateReview(review);
        eventService.createEvent(reviewUpdated.getUserId(),
                Math.toIntExact(reviewUpdated.getReviewId()),
                EventType.REVIEW,
                EventOperation.UPDATE);
        return reviewUpdated;
    }

    public long deleteReviewById(Long id) {
        Review review = findReviewById(id);
        if (review.getUserId() <= 0) {
            throw new MethodArgumentNotException("Id пользователя не может быть равен 0 или быть отрицательным");
        }
        eventService.createEvent(review.getUserId(), Math.toIntExact(id), EventType.REVIEW, EventOperation.REMOVE);
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