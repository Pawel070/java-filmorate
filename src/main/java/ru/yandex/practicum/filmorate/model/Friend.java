package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Builder
public class Friend {

    @NotNull
    private int idUser;

    @NotNull
    private int idFriend;

    @NotNull
    private String idType;

    public Friend(int idUser, int idFriend, String idType) {
        this.idUser = idUser;
        this.idFriend = idFriend;
        this.idType = idType;
    }
}