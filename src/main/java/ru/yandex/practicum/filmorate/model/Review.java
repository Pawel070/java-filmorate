package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Review {
    private long reviewId;

    @NonNull
    private int filmId;

    @NonNull
    private int userId;

    @NonNull
    private String content;

    @NonNull
    private Boolean isPositive;

    @JsonProperty("isPositive")
    public Boolean getIsPositive() {
        return isPositive;
    }

    private Long useful;
}