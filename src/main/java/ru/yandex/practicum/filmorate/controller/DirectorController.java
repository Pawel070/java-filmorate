package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.Map;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.yandex.practicum.filmorate.Service.DirectorService;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;


@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController implements ControllerInterface<Director> {

    private final DirectorStorage directorStorage;
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorStorage directorStorage, DirectorService directorService) {
        this.directorStorage = directorStorage;
        this.directorService = directorService;
    }

    @GetMapping("/help")
    public Map<String, Integer> feed() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Метод /feed ещё не реализован.");
    }

    @Override
    @GetMapping("/{id}")
    public Director getById(@PathVariable int id) {  // режисёр по iD
        log.info("Контроллер GET режисёр по Id> {}", id);
        return  directorStorage.getByIdDirector(id);
    }

    @Override
    @GetMapping
    public Collection<Director> selectGetting() {
        log.info("Запрос всех режисёров.");
        return  directorStorage.getCollectionDirector();
    }

    @Override
    @PutMapping
    public Director update(@RequestBody @Valid Director director) {
        log.info("Обновление режисёра {}", director);
        validate(director);
        return  directorStorage.update(director);
    }

    @Override
    @PostMapping
    public Director create(@RequestBody @Valid Director director) {
        log.info("Добавление режисёра  {}", director);
        validate(director);
        return directorStorage.create(director);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("Удаление режисёра из базы по id {}", id);
        directorStorage.getByIdDirector(id);
    }

    @Override
    public void validate(Director director) {
        directorService.validateD(director);
    }

}
