package com.epam.brest.dao.constant;

public enum ParamName {
    ID, NUMBER;

    public String getName() {
       return name().toLowerCase();
    }

}
