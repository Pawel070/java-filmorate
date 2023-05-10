package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.Service.GenreService;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController implements ControllerInterface<Genre> {

    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/genres")
    public Collection<Genre> selectGetting() {
        log.info("Контроллер GET список жанров.");
        return service.findAll();
    }

    @GetMapping("/genres/{id}")
    protected List<Genre> findById(@PathVariable int id) {
        log.info("Контроллер GET список жанров фильма по Id {} ", id);
        return service.findById(id);
    }

    @Override
    public Genre create(Genre genre) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод ещё не реализован.");
    }

    @Override
    public Genre update(Genre genre) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод ещё не реализован.");
    }

    @Override
    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод ещё не реализован.");
    }

    @Override
    public void delete(int id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод ещё не реализован.");
    }

}
