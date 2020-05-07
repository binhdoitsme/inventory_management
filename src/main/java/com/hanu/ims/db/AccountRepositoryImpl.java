package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.mapper.AccountMapper;
import com.hanu.ims.model.repository.AccountRepository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl extends RepositoryImpl<Account, Integer>
        implements AccountRepository {

    private final AccountMapper mapper;

    public AccountRepositoryImpl() {
        mapper = new AccountMapper();
    }

    @Override
    public boolean add(Account account) {
        String sql = "INSERT INTO account (`username`, `password`, `role`) VALUES ('$username', '$password', '$role')"
                .replace("$username", account.getUsername())
                .replace("$password", account.getPassword())
                .replace("$role", account.getRole().name());

        try {
            int affectedRows = getConnector().connect().executeInsert(sql);
            if (affectedRows != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void add(List<Account> items) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Account item) {

    }

    @Override
    public void deleteAll(List<Account> items) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public boolean existById(Integer integer) {
        return false;
    }

    @Override
    public Account findById(Integer integer) {
        return null;
    }

    @Override
    public List<Account> findAllById(List<Integer> integers) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Account> findAll() { //currently omitting admins
        List<Account> accountList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM account WHERE (role != \"Admin\")";
            ResultSet rs = getConnector().connect().executeSelect(sql);
            while (rs.next()) {
                Account account = mapper.forwardConvert(rs);
                if (account == null) {
                    continue;
                }
                accountList.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountList;
    }

    @Override
    public Account save(Account item) {
        return null;
    }

    @Override
    public List<Account> saveAll(List<Account> items) {
        return null;
    }

    @Override
    public Account findById(int id) {
        return null;
    }

    @Override
    public Account findByUsernameAndPassword(String username, String password) {
        try {
            String sql = "SELECT * FROM account WHERE (username = '$username' and password = '$password')"
                    .replace("$username", username)
                    .replace("$password", password);
            ResultSet rs = getConnector().connect().executeSelect(sql);
            rs.next();
            return mapper.forwardConvert(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
