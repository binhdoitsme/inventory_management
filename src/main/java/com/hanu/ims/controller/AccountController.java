package com.hanu.ims.controller;

import com.hanu.ims.db.AccountRepositoryImpl;
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
