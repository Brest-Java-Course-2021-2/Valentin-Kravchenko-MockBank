package com.epam.brest.dao.api;

import com.epam.brest.model.BasicEntity;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends BasicEntity> {

    /**
     * Retrieves all entities.
     * @return the list of all entities
     */
    List<T> getAll();

    /**
     * Retrieves an entity by its ID.
     * @param id - entity ID
     * @return the entity with the given id or empty optional if none found
     */
    Optional<T> getById(Integer id);

    /**
     * Retrieves an entity by its number.
     * @param number - entity number
     * @return the entity with the given number or empty optional if none found
     */
    Optional<T> getByNumber(String number);

    /**
     * Creates a given entity.
     * @param entity - entity instance
     * @return the instance of the created entity
     */
    T create(T entity);

    /**
     * Updates a given entity.
     * @param entity - entity instance
     * @return the number of rows affected
     */
    Integer update(T entity);

    /**
     * Removes an entity by its id.
     * @param id - ID of the entity to delete
     * @return the ID of the deleted entity
     */
    Integer delete(Integer id);

    /**
     * Returns the number of entities available.
     * @return the number of entities
     */
    Integer count();

}