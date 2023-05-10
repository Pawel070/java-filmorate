package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.dataService.FriendsData;

@Data
@Validated
@Builder
public class Friend {

    private int id;

    @NotNull
    private int idUser;

    @NotNull
    private int idFriend;

    @NotNull
    private FriendsData idType;

    public Friend(int id, int idUser, int idFriend, FriendsData idType) {
        this.id = id;
        this.idUser = idUser;
        this.idFriend = idFriend;
        this.idType = idType;
    }
}
