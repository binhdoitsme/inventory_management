package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.InvalidQueryTypeException;
import com.hanu.ims.model.domain.Supplier;
import com.hanu.ims.model.mapper.SupplierMapper;
import com.hanu.ims.model.repository.SupplierRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl extends RepositoryImpl<Supplier, Integer> implements SupplierRepository {
    private SupplierMapper supplierMapper;

    public SupplierRepositoryImpl() {
        supplierMapper = new SupplierMapper();
    }

    @Override
    public boolean add(Supplier supplier) {
        String query = "INSERT INTO supplier(name, phone, address, is_available) VALUES $value"
                .replace("$value", supplierMapper.convert(supplier));
        try {
            int effectedRow = getConnector().connect().executeInsert(query);
            if (effectedRow != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean add(List<Supplier> items) {
        List<String> lstquery = new ArrayList<>();
        for (Supplier s : items) {
            lstquery.add(supplierMapper.convert(s));
        }
        String query = "INSERT INTO supplier(name, phone, address, is_available) VALUES $value"
                .replace("$value", new String().join(",", lstquery));
        try {
            if (getConnector().connect().executeInsert(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean delete(Supplier item) {
        String query = "DELETE FROM Supplier WHERE id = $id"
                .replace("$id", "\'" + item.getId() + "\'");
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAll(List<Supplier> items) {
        List<Integer> lstid = new ArrayList<>();
        for (Supplier s : items) {
            lstid.add(s.getId());
        }
        String query = "DELETE FROM Supplier WHERE id IN ($value)"
                .replace("$value", new String().join(",", "\'" + lstid + "\'"));
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        String query = "DELETE FROM Supplier";
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer integer) {
        String query = "DELETE FROM Supplier WHERE id = $id"
                .replace("$id", "\'" + integer + "\'");
        try {
            if (getConnector().connect().executeDelete(query) != 0) {
                return true;
            }
            return false;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return false;
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
        String query = "SELECT * FROM Supplier WHERE id = $id"
                .replace("$id", "\'" + integer + "\'");
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            return supplierMapper.forwardConvert(rs);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Supplier> findAllById(List<Integer> integers) {
        List<Supplier> lstSupplier = new ArrayList<>();
        String query = "SELECT * FROM Supplier WHERE id IN ($value)"
                .replace("$value", new String().join(",", "\'" + integers + "\'"));
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            while (rs.next()) {
                lstSupplier.add(supplierMapper.forwardConvert(rs));
            }
            return lstSupplier;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Supplier> findAll() {
        List<Supplier> lstSupplier = new ArrayList<>();
        String query = "SELECT * FROM Supplier";
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            while (rs.next()) {
                lstSupplier.add(supplierMapper.forwardConvert(rs));
            }
            return lstSupplier;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Supplier findById(int id) {
        return null;
    }

    @Override
    public Supplier save(Supplier item) {
        String query = "UPDATE Supplier SET name = $name, phone = $phone, address = $address, is_available = $is_available WHERE id = $id"
                .replace("$name", "\'" + item.getName() + "\'")
                .replace("$phone", "\'" + item.getPhone() + "\'")
                .replace("$address", "\'" + item.getAddress() + "\'")
                .replace("$is_available", "\'" + item.getIs_available() + "\'")
                .replace("$id", "\'" + item.getId() + "\'");
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
            return item;
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
            return null;
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
        String query = "UPDATE Supplier SET is_available = false WHERE id = $id"
                .replace("$id", "\'" + id + "\'");
        try {
            ResultSet rs = getConnector().connect().executeSelect(query);
        } catch (SQLException | InvalidQueryTypeException e) {
            e.printStackTrace();
        }
    }
}

