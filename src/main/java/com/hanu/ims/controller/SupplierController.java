package com.hanu.ims.controller;

import com.hanu.ims.db.SupplierRepositoryImpl;
import com.hanu.ims.model.domain.Supplier;
import com.hanu.ims.model.repository.SupplierRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;

import java.util.List;

public class SupplierController {
    private SupplierRepository repository;

    public SupplierController() {
        repository = ServiceContainer.locateDependency(SupplierRepository.class);
    }

    public boolean createSupplier(Supplier supplier) throws Exception {
        if (isvalidated(supplier)) {
            repository.add(supplier);
            return true;
        }
        return false;
    }

    public boolean invalidateSupplier(Supplier supplier) throws Exception {
        if (isvalidated(supplier)) {
            repository.invalidate(getSupplierDetails(supplier.getId()).getId());
            return true;
        }
        return false;
    }

    public Supplier getSupplierDetails(int id) throws Exception {
        return repository.findById(id);
    }

    public void updateSupplier(Supplier supplier) throws Exception {
        repository.save(supplier);
    }

    public List<Supplier> getSupplierList()  throws Exception {
        return repository.findAll();
    }
    
    //helper

    public boolean isIn(Supplier supplier) throws Exception {
        if (getSupplierDetails(supplier.getId()) != null) {
            return true;
        }
        return false;
    }

    public boolean isvalidated(Supplier supplier) throws Exception {
        if (supplier != null || !isIn(supplier)) {
            return true;
        } else {
            return false;
        }
    }
}