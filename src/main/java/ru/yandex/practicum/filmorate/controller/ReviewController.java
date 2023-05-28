package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Service.ReviewService;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Data
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addReview(@RequestBody @Valid Review review) {
        Review request = reviewService.addReview(review);
        log.debug("Добавление пользователем id = {} отзыва к фильму id = {}", review.getUserId(), review.getFilmId());
        return request;
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review) {
        Review request = reviewService.updateReview(review);
        log.debug("Правка пользователем id = {} отзыва id = {} к фильму id ={}", review.getUserId(),
                review.getReviewId(), review.getFilmId());
        return request;
    }

    @DeleteMapping("/{id}")
    long deleteReviewById(@PathVariable long id) {
        long request = reviewService.deleteReviewById(id);
        log.debug("Удаление отзыва id = {}", id);
        return request;
    }

    @GetMapping("/{id}")
    public Review findReviewById(@PathVariable long id) {
        Review request = reviewService.findReviewById(id);
        log.debug("Получение отзыва id = {}, к фильму id = {} от пользователя id = {}", id,
                request.getFilmId(), request.getUserId());
        return request;
    }

    @GetMapping
    public List<Review> findAllReviews(@RequestParam(defaultValue = "0") int filmId,
                                       @RequestParam(defaultValue = "10") int count) {
        List<Review> request = reviewService.findAllReviews(filmId, count);
        if (filmId == 0) {
            log.debug("Получение отзывов ко всем фильмам. В количестве не более {}", count);
        } else {
            log.debug("Получение отзывов к фильму id = {}. В количестве не более {}", filmId, count);
        }
        return request;
    }

    @PutMapping("{id}/like/{userId}")
    public long likeReview(@PathVariable long id, @PathVariable int userId) {
        long request = reviewService.likeReview(userId, id);
        log.debug("Пользователь id = {} поставил лайк отзыву id = {}", userId, id);
        return request;
    }

    @PutMapping("{id}/dislike/{userId}")
    public long dislikeReview(@PathVariable long id, @PathVariable int userId) {
        long request = reviewService.dislikeReview(userId, id);
        log.debug("Пользователь id = {} поставил дизлайк отзыву id = {}", userId, id);
        return request;
    }

    @DeleteMapping("{id}/like/{userId}")
    public long deleteLikeReview(@PathVariable long id, @PathVariable int userId) {
        long request = reviewService.deleteLikeReview(userId, id);
        log.debug("Пользователь id = {} удалил свой лайк отзыву id = {}", userId, id);
        return request;
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public long deleteDislikeReview(@PathVariable long id, @PathVariable int userId) {
        long request = reviewService.deleteDislikeReview(userId, id);
        log.debug("Пользователь id = {} удалил свой дизлайк отзыву id = {}", userId, id);
        return request;
    }
}