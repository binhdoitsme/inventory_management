package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

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
                    getUnix(rs.getTimestamp("last_update"))
            );
        } catch (SQLException e) {
            return null;
        }
    }

    private static Long getUnix(Timestamp timestamp) {
        long result = timestamp.toInstant().getEpochSecond();
        return result;
    }
}
