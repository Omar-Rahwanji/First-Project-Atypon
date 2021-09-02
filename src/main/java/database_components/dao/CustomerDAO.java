package database_components.dao;

import database_components.Entity;
import database_components.entities.Customer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomerDAO implements DAO{
    private final Lock lock = new ReentrantLock();

    @Override
    public String recordToString(Entity row){
        var customer = (Customer) row;
        return customer.getId()+","+ customer.getName()+","+ customer.getCountry()+","+ customer.getPhone()+","+ customer.getEmail();
    }

    @Override
    public void setAttributeValue(Entity row, String attribute, String newValue) {
        lock.lock();
        var customer = (Customer) row;
        switch(attribute){
            case "id":
                customer.setId(Integer.parseInt(newValue));
                break;
            case "name":
                customer.setName(newValue);
                break;
            case "country":
                customer.setCountry(newValue);
                break;
            case "phone":
                customer.setPhone(Integer.parseInt(newValue));
                break;
            case "email":
                customer.setEmail(newValue);
                break;
            default:
                break;
        }
        lock.unlock();
    }

    public String getValueByName(Customer customer, String name) {
        return switch (name) {
            case "id" -> Integer.toString(customer.getId());
            case "name" -> customer.getName();
            case "country" -> customer.getCountry();
            case "phone" -> Integer.toString(customer.getPhone());
            case "email" -> customer.getEmail();
            default -> "";
        };
    }
}
