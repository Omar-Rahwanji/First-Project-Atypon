package database_components;

import database_components.entities.Account;
import database_components.entities.Customer;
import database_components.entities.NullEntity;

public class EntityFactory {
    private static EntityFactory entityFactory;

    private EntityFactory() {
    }

    public static EntityFactory getEntityFactory() {
        if (entityFactory == null)
            entityFactory = new EntityFactory();
        return entityFactory;
    }

    public Entity getEntityByType(String type) {
        if(type.equalsIgnoreCase("customer"))
            return new Customer();
        else if(type.equalsIgnoreCase("account"))
            return new Account();
        return NullEntity.getInstance();
    }

}
