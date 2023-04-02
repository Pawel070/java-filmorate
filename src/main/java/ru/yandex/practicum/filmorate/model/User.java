package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
public class User {
    private String name;
    private int id;
    private String login;
    private String email;
    private LocalDateTime birthday;

}
