package com.epam.brest.service;

import com.epam.brest.model.BaseEntity;

public interface GenericService<T extends BaseEntity> {

    /**
     * Returns an entity by its id.
     * @param id - entity id
     * @return the entity with the given id
     * @throws IllegalArgumentException if none found
     */
    T getById(Integer id);

}
