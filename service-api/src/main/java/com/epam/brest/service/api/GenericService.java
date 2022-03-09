package com.epam.brest.service.api;

import com.epam.brest.model.BasicEntity;
import com.epam.brest.service.exception.ResourceNotFoundException;

public interface GenericService<T extends BasicEntity> {

    /**
     * Returns an entity by its id.
     * @param id - entity id
     * @return the entity with the given id
     * @throws ResourceNotFoundException if none found
     */
    T getById(Integer id);

}
