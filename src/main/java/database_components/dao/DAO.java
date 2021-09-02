package database_components.dao;

import database_components.Entity;

public interface DAO {
    String recordToString(Entity row);
    void setAttributeValue(Entity row, String attribute, String newValue);
}
