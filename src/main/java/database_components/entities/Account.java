package database_components.entities;

import database_components.Entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account extends Entity {
    private int id;
    private int customerId;
    private String username;
    private final Lock lock = new ReentrantLock();
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setAttributes(String[] attributes) {
        lock.lock();
        setId(Integer.parseInt(attributes[0]));
        setCustomerId(Integer.parseInt(attributes[1]));
        setUsername(attributes[2]);
        lock.unlock();
    }
}
