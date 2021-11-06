package dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    List<T> getAll();

    Optional<T> getOneById(Integer id);

    T create(T entity);

    T update(T entity);

    Integer delete(Integer id);

}
