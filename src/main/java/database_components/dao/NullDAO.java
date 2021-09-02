package database_components.dao;

import database_components.Entity;

public class NullDAO implements DAO{
    @Override
    public String recordToString(Entity row) {
        //This method is unsupported
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttributeValue(Entity row, String attribute, String newValue) {
        //This method is unsupported
        throw new UnsupportedOperationException();
    }
}
