package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ru.yandex.practicum.filmorate.Service.RateService;
import ru.yandex.practicum.filmorate.model.Rating;


@Slf4j
@RestController
@RequestMapping("/mpa")
public class RateController {

    private final RateService service;

    @Autowired
    public RateController(RateService service) {
        this.service = service;
    }


    @GetMapping("")
    public Collection<Rating> selectGetting() {
        log.info("Контроллер GET список рейтингов.");
        return service.findAll();
    }


    @GetMapping("/{id}")
    public Rating getById(@PathVariable int id) {
        log.info("Контроллер GET рейтинг по Id {} ", id);
        return service.findById(id);
    }

}