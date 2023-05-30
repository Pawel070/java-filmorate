package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
public class Director {   // 😉

    private int id;

    @Size(message = "Имя режисера не менее трех символов", min = 3)
    private String name;

    public Director(int id, String name) {
        this.id = id;
        this.name = name;
    }
}