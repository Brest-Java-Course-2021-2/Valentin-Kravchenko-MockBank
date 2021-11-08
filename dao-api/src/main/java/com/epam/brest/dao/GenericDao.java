package com.epam.brest.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    /**
     * Retrieves all entities
     *
     * @return list of all entities
     */
    List<T> getAll();

    /**
     * Retrieves an entity by its id
     * @param id - entity id
     * @return the entity with the given id or empty optional if none found
     */
    Optional<T> getOneById(Integer id);

    /**
     * Creates a given entity
     * @param entity - entity instance
     * @return the created entity
     */
    T create(T entity);

    /**
     * Updates a given entity
     * @param entity - entity instance
     * @return the number of rows affected
     */
    Integer update(T entity);

    /**
     * Removes an entity by its id
     * @param id - entity id
     * @return the number of rows affected
     */
    Integer delete(Integer id);

    /**
     * Return the number of entities available
     * @return the number of entities
     */
    Integer count();

}
