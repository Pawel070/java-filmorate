package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private long reviewId;

    @NotNull
    private int filmId;

    @NotNull
    private int userId;

    @NotNull
    private String content;

    @NotNull
    private boolean isPositive;

    @JsonProperty("isPositive")
    public boolean getIsPositive() {
        return isPositive;
    }

    private Long useful;
}