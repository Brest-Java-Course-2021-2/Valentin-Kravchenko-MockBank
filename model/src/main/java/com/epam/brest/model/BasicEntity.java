package com.epam.brest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 *  Basic entity model.
 */
public abstract class BasicEntity {

    /**
     *  Entity ID.
     */
    @Schema(description = "ID", example = "1")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicEntity that = (BasicEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
