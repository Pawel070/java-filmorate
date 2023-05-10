package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.ErrorsIO.ItemNotFoundException;
import ru.yandex.practicum.filmorate.ErrorsIO.ValidationException;
import ru.yandex.practicum.filmorate.model.GlobalAbstractBon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Validated
@Repository
@Component
public abstract class GlobalAbstractInMemoryStorage<T extends GlobalAbstractBon> { // в log указан GAIMS
    private final Map<Long, T> storageAbstract = new HashMap<>();
    private int numberId;

    abstract void validate(T item) throws ValidationException;

    public void create(T atom) {
        if (storageAbstract.containsKey(atom.getId())) {
            throw new IllegalArgumentException("GAIMS > " + atom.getClass().getSimpleName() + " с таким id уже существует");
        } else {
            validate(atom);
            atom.setId(servisId());
            storageAbstract.put(atom.getId(), atom);
        }
    }

    public void update(T item) {
        validate(item);
        if (storageAbstract.containsKey(item.getId())) {
            storageAbstract.put(item.getId(), item);
        } else {
            throw new ItemNotFoundException("GAIMS > " + item.getClass().getSimpleName() + " с указанным id не найден");
        }
    }

    public void delete(long id) {
        if (storageAbstract.containsKey(id)) {
            storageAbstract.remove(id);
        } else {
            log.info("GAIMS > Пользователь с id {} не найден", id);
            throw new ItemNotFoundException("Пользователь с указанным id " + id + " не найден");
        }
    }

    public T get(long id) {
        if (storageAbstract.containsKey(id)) {
            return storageAbstract.get(id);
        } else {
            log.info("GAIMS > Пользователь с id {} не найден", id);
            throw new ItemNotFoundException("Пользователь с указанным id " + id + " не найден");
        }
    }

    public Collection<T> getAll() {
        return storageAbstract.values();
    }

    private int servisId() {
        numberId++;
        log.info("set GAIMS ServisId: {}", numberId);
        return numberId;
    }

}
