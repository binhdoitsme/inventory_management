package com.hanu.ims.controller;

import com.hanu.ims.db.AccountRepositoryImpl;
import com.hanu.ims.exception.InvalidQueryTypeException;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.repository.AccountRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;

import java.sql.SQLException;
import java.util.List;

public class AccountController {
    private AccountRepository repository;

    public AccountController() {
        repository = ServiceContainer.locateDependency(AccountRepository.class);
    }

    public Account validate(String username, String password) {
        Account validatedAccount = repository.findByUsernameAndPassword(username, password);
        return validatedAccount;
    }

    public long countUsers() throws SQLException, InvalidQueryTypeException {
        return repository.count();
    }

    public boolean deleteAccount(Account account){
        return repository.delete(account);
    }

    public boolean updateAccount(Account account){
        Account status= repository.save(account);
        if(status==null) return false;
        else return true;
    }

    public List<Account> getAccountList() {
        return repository.findAll();
    }

    public void createAccount(Account account) throws Exception {
        boolean result = repository.add(account);
        if (!result) {
            throw new SQLException();
        }
    }
}
