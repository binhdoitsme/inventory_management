package com.hanu.ims.model.repository;

import com.hanu.ims.base.Repository;
import com.hanu.ims.model.domain.Supplier;

import java.util.List;


public interface SupplierRepository extends Repository<Supplier, Integer> {
    List<Supplier> lst = null;
    boolean add(Supplier supplier);
    void invalidate(int id);
    List<Supplier> findAll();
    Supplier findById(int id);
}
