package database_components.dao;

import cache_components.Cache;
import database_components.Entity;
import database_components.entities.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountDAO implements DAO{
    private final Lock lock = new ReentrantLock();

    @Override
    public String recordToString(Entity row){
        var account = (Account) row;
        return account.getId()+","+ account.getCustomerId()+","+ account.getUsername();
    }

    @Override
    public void setAttributeValue(Entity row, String attribute, String newValue) {
        lock.lock();
        var account = (Account) row;
        switch(attribute){
            case "id":
                account.setId(Integer.parseInt(newValue));
                break;
            case "customerId":
                account.setCustomerId(Integer.parseInt(newValue));
                break;
            case "username":
                account.setUsername(newValue);
                break;
            default:
                break;
        }
        lock.unlock();
    }

    public List<Account> getAccounts(int customerId, List<Cache> cachedRecords){
        var accounts = cachedRecords.get(1);
        List<Account> customerAccounts = new ArrayList<>();
        for (int accountId : accounts.getAllKeys()) {
            var account = (Account) accounts.get(accountId);
            if(account.getCustomerId() == customerId)
                customerAccounts.add(account);
        }
        return  customerAccounts;
    }
}
