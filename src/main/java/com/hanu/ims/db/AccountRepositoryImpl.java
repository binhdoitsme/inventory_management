package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.exception.InvalidQueryTypeException;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.mapper.AccountMapper;
import com.hanu.ims.model.repository.AccountRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AccountRepositoryImpl extends RepositoryImpl<Account, Integer>
        implements AccountRepository {

    private final AccountMapper mapper;

    public AccountRepositoryImpl() {
        mapper = new AccountMapper();
    }

    @Override
    public boolean add(Account account) {
        String sql = "INSERT INTO account (`username`, `password`, `role`, `last_update`) VALUES ('$username', '$password', '$role', '$last_update')"
                .replace("$username", account.getUsername())
                .replace("$password", account.getPassword())
                .replace("$role", account.getRole().name())
                .replace("$last_update", String.valueOf(getCurrentTimeStamp()));

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
    public boolean add(List<Account> items) {
        return false;
    }

    @Override
    public long count() { //total count, including admins
        long count = 0;
        try {
            String sql = "SELECT * FROM account";
            count = getConnector().connect().executeScalar(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidQueryTypeException e) {
            e.printStackTrace();
        }
        return count;

//        ResultSet rs = null;
//        try {
//            rs = getConnector().connect().executeSelect(sql);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (InvalidQueryTypeException e) {
//            e.printStackTrace();
//        }
//        while (true) {
//            try {
//                if (!rs.next()) break;
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//            ++count;
//        }

    }

    @Override
    public boolean delete(Account item) {
        String sql = "DELETE FROM account WHERE id='$id'".replace("$id", String.valueOf(item.getId()));
        try {
            return getConnector().connect().executeDelete(sql) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean deleteAll(List<Account> items) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean deleteById(Integer integer) {
        return false;
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

        /*
String sql = "DELETE FROM ACCOUNT WHERE id= '$id' and username= '$username' and password= '$password' and role= '$role'"
                    .replace("$id", Integer.toString(item.getId()))
                    .replace("$username", item.getUsername())
                    .replace("$password", item.getPassword())
                    .replace("$role", item.getRole().name());

     */

    @Override
    public Account save(Account item) {
        boolean updateCount = false;
        try {
            String sql =
                    "update account set username= '$username', password= '$password' , last_update= '$last_update' where id= $id"
                            .replace("$username", item.getUsername())
                            .replace("$password", item.getPassword())
                            .replace("$last_update", String.valueOf(getCurrentTimeStamp()))
                            .replace("$id", Integer.toString(item.getId()));

            updateCount = getConnector().connect().executeUpdate(sql) > 0;
            System.out.println("updateCount is " + updateCount);
            if (updateCount) {
                return item;
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidQueryTypeException e) {
            e.printStackTrace();
        }
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

    public Timestamp getCurrentTimeStamp() {
        long timeMillis = System.currentTimeMillis();
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
        return new Timestamp(timeSeconds * 1000);
    }
}
