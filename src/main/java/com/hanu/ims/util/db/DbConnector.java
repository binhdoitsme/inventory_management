package com.hanu.ims.util.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hanu.ims.exception.InvalidQueryTypeException;
//import com.hanu.ims.util.di.Dependency;

/**
 * Handle database connection
 */
//@Dependency
public interface DbConnector {
    DbConnector connect() throws SQLException;
    ResultSet executeSelect(String query) throws SQLException, InvalidQueryTypeException;
    <T> T executeScalar(String query) throws SQLException, InvalidQueryTypeException;
    int executeInsert(String query) throws SQLException, InvalidQueryTypeException;
    int executeUpdate(String query) throws SQLException, InvalidQueryTypeException;
    int executeDelete(String query) throws SQLException, InvalidQueryTypeException;
    void beginTransaction() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
}