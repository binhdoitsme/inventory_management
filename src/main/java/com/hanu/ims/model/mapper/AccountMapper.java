package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper extends Mapper<Account> {

    public AccountMapper() {
        super(AccountMapper::fromResultSet);
    }

    private static Account fromResultSet(ResultSet rs) {
        try {
            return new Account(rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role")),
                    rs.getLong("last_update")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
