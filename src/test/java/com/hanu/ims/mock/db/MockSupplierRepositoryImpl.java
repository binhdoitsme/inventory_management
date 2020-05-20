package com.hanu.ims.mock.db;

import com.hanu.ims.db.SupplierRepositoryImpl;
import com.hanu.ims.model.domain.Supplier;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MockSupplierRepositoryImpl extends SupplierRepositoryImpl {
    private Hashtable<Integer, Supplier> map;

    public MockSupplierRepositoryImpl() {
        map = new Hashtable<Integer, Supplier>();
        map.put(1, new Supplier(1, "Supplier1", "0832677917", "KTX", false));
        map.put(2, new Supplier(2, "Supplier2", "0832677917", "KTX", true));
        map.put(3, new Supplier(3, "Supplier3", "0832677917", "KTX", false));
    }

    @Override
    public boolean add(Supplier supplier) {
        map.put(supplier.getId(), supplier);
        return true;
    }

    @Override
    public boolean add(List<Supplier> items) {
        for (Supplier item : items) {
            map.put(item.getId(), item);
            return true;
        }
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean delete(Supplier item) {
        map.remove(item.getId());
        return true;
    }

    @Override
    public boolean deleteAll(List<Supplier> items) {
        for (Supplier item : items) {
            map.remove(item.getId());
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        map.clear();
        return true;
    }

    @Override
    public boolean deleteById(Integer integer) {
        map.remove(integer);
        return true;
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
        return map.get(integer);
    }

    @Override
    public List<Supplier> findAllById(List<Integer> integers) {
        List<Supplier> lstSupplier = new ArrayList<>();
        for (int id : integers) {
            Supplier s = findById(id);
            if (s != null) {
                lstSupplier.add(s);
            }
        }
        return lstSupplier;
    }

    @Override
    public List<Supplier> findAll() {
        List<Supplier> lstSupplier = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            lstSupplier.add(map.get(i));
        }
        return lstSupplier;
    }

    @Override
    public Supplier save(Supplier item) {
        if (findById(item.getId()) != null) {
            map.replace(item.getId(), item);
            return item;
        }
        return null;
    }

    @Override
    public List<Supplier> saveAll(List<Supplier> items) {
        List<Supplier> lstupdated = new ArrayList<Supplier>();
        for (Supplier s : items) {
            if (findById(s.getId()) != null) {
                map.replace(s.getId(), s);
                lstupdated.add(s);
            }
        }
        return items;
    }

    @Override
    public void invalidate(int id) {
        Supplier s = findById(id);
        if (s != null) {
            s.setIsAvailable(false);
        }
    }
}
