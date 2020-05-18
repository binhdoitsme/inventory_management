package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.exception.InvalidQueryTypeException;
import com.hanu.ims.model.domain.Supplier;
import com.hanu.ims.model.mapper.SupplierMapper;
import com.hanu.ims.model.repository.SupplierRepository;
import com.hanu.ims.util.configuration.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl extends RepositoryImpl<Supplier, Integer> implements SupplierRepository {
    // constants
    private static final String ADD_ONE = Configuration.get("db.sql.supplier.addOne");
    private static final String ADD_MANY = Configuration.get("db.sql.supplier.addMany");
    private static final String DELETE_ONE = Configuration.get("db.sql.supplier.deleteOne");
    private static final String DELETE_MANY = Configuration.get("db.sql.supplier.deleteMany");
    private static final String DELETE_ALL = Configuration.get("db.sql.supplier.deleteAll");
    private static final String DELETE_BY_ID = Configuration.get("db.sql.supplier.deleteById");
    private static final String FIND_BY_ID = Configuration.get("db.sql.supplier.findById");
    private static final String FIND_ALL = Configuration.get("db.sql.supplier.findAll");
    private static final String FIND_ALL_BY_ID = Configuration.get("db.sql.supplier.findAllById");
    private static final String FIND_ALL_ACTIVE = Configuration.get("db.sql.supplier.findAllActiveSuppliers");
    private static final String SAVE_ONE = Configuration.get("db.sql.supplier.saveOne");
    private static final String INVALIDATE = Configuration.get("db.sql.supplier.invalidate");

    // mappers
    private SupplierMapper supplierMapper;

    public SupplierRepositoryImpl() {
        supplierMapper = new SupplierMapper();
    }

    @Override
    public boolean add(Supplier supplier) {
        String query = ADD_ONE
                .replace("$value", supplierMapper.convert(supplier));
        try {
            var connector = getConnector().connect();
            int effectedRow = connector.executeInsert(query);
//            int id = connector.executeScalar("SELECT MAX(id) FROM supplier");
//            connector.executeInsert("INSERT INTO supplier_category VALUES ('$sid')"
//                        .replace("$sid", String.valueOf(id)));
            if (effectedRow != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean add(List<Supplier> items) {
        List<String> lstquery = new ArrayList<>();
        for (Supplier s : items) {
            lstquery.add(supplierMapper.convert(s));
        }
        String query = ADD_MANY
                .replace("$value", new String().join(",", lstquery));
        try {
            if (getConnector().connect().executeInsert(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean delete(Supplier item) {
        String query = DELETE_ONE
                .replace("$id", "\'" + item.getId() + "\'");
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean deleteAll(List<Supplier> items) {
        List<Integer> lstid = new ArrayList<>();
        for (Supplier s : items) {
            lstid.add(s.getId());
        }
        String query = DELETE_MANY
                .replace("$value", new String().join(",", "\'" + lstid + "\'"));
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean deleteAll() {
        String query = DELETE_ALL;
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean deleteById(Integer integer) {
        String query = DELETE_BY_ID
                .replace("$id", "\'" + integer + "\'");
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean existById(Integer integer) {
        if (findById(integer) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Supplier findById(Integer integer) {
        String query = FIND_BY_ID
                .replace("$id", "\'" + integer + "\'");
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            return supplierMapper.forwardConvert(rs);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public List<Supplier> findAllById(List<Integer> integers) {
        List<Supplier> lstSupplier = new ArrayList<>();
        String query = FIND_ALL_BY_ID
                .replace("$value", new String().join(",", "\'" + integers + "\'"));
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            while (rs.next()) {
                lstSupplier.add(supplierMapper.forwardConvert(rs));
            }
            return lstSupplier;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public List<Supplier> findAll() {
        List<Supplier> lstSupplier = new ArrayList<>();
        String query = FIND_ALL;
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            while (rs.next()) {
                lstSupplier.add(supplierMapper.forwardConvert(rs));
            }
            return lstSupplier;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public Supplier save(Supplier item) {
        String query = SAVE_ONE
                .replace("$name", "\'" + item.getName() + "\'")
                .replace("$phone", "\'" + item.getPhone() + "\'")
                .replace("$address", "\'" + item.getAddress() + "\'")
                .replace("$is_available", "\'" + (item.isAvailable() ? 1 : 0) + "\'")
                .replace("$id", "\'" + item.getId() + "\'");
        try {
            var rowsAffected = getConnector().connect().executeUpdate(query);
            if (rowsAffected > 0) {
                return findById(item.getId());
            }
            return item;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public List<Supplier> saveAll(List<Supplier> items) {
        List<Supplier> lstSup = new ArrayList<>();
        for (Supplier s : items) {
            if (save(s) != null) {
                lstSup.add(s);
            }
        }
        return items;
    }

    @Override
    public void invalidate(int id) {
        String query = INVALIDATE
                .replace("$id", "\'" + id + "\'");
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Supplier> findAllActiveSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        String query = FIND_ALL_ACTIVE;
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            while (rs.next()) {
                var supplier = supplierMapper.forwardConvert(rs);
                if (supplier == null) continue;
                supplierList.add(supplier);
            }
            return supplierList;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }
}

