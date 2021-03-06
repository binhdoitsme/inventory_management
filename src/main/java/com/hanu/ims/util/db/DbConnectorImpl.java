package com.hanu.ims.util.db;

import com.hanu.ims.exception.InvalidQueryTypeException;
import com.hanu.ims.util.configuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnectorImpl implements DbConnector {
    // constants
    private static final String DRIVER = Configuration.get("db.driver");
    private static final String CONNECTION_STRING = Configuration.get("db.connectionstring");
    private static final String DB_USER = Configuration.get("db.user");
    private static final String DB_PASSWORD = Configuration.get("db.password");

    private static final Logger logger = Logger.getLogger(DbConnectorImpl.class.getName());

    private Connection connection;

    public DbConnectorImpl() throws ClassNotFoundException {
        loadDriver();
    }

    private void loadDriver() throws ClassNotFoundException {
        Class.forName(DRIVER);
    }

    @Override
    public DbConnector connect() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        return this;
    }

    @Override
    public ResultSet executeSelect(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.SELECT);
        logQuery(query);
        return connection.createStatement().executeQuery(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Object> T executeScalar(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.SELECT);
        logQuery(query);
        ResultSet rs = connection.createStatement().executeQuery(query);
        // scalar query returns one value only
        rs.next();
        try {
            return (T) rs.getObject(1);
        } catch (Exception e) {
            logger.info(e.getClass().getName());
            return null;
        }
    }

    @Override
    public int executeInsert(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.INSERT);
        logQuery(query);
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public int executeUpdate(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.UPDATE);
        logQuery(query);
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public int executeDelete(String query) throws SQLException, InvalidQueryTypeException {
        throwExceptionIfInvalidQueryType(query, DbQueryType.DELETE);
        logQuery(query);
        return connection.createStatement().executeUpdate(query);
    }

    private void throwExceptionIfInvalidQueryType(String query, DbQueryType type)
            throws InvalidQueryTypeException {
        if (!validateQueryType(query, type)) {
            throw new InvalidQueryTypeException();
        }
    }

    private boolean validateQueryType(String query, DbQueryType type) {
        return query.toLowerCase().trim().startsWith(type.name().toLowerCase());
    }

    @Override
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public static void logQuery(String query) {
        logger.info(query);
    }
}

enum DbQueryType {
    SELECT, INSERT, UPDATE, DELETE
}