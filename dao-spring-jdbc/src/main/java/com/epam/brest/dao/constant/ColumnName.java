package com.epam.brest.dao.constant;

public enum ColumnName {
    ID, NUMBER;

    public String getName() {
       return name().toLowerCase();
    }

}
