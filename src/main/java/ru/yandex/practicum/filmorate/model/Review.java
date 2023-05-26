package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Valid
@Builder
public class Review {
    private long reviewId;

    @NotNull
    private int filmId;

    @NotNull
    @Positive
    private int userId;

    @NotNull
    private String content;

    private boolean isPositive;

    @JsonProperty("isPositive")
    public boolean getIsPositive() {
        return isPositive;
    }

    private Long useful;
}