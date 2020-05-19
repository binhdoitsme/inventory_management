package com.hanu.ims.controller;

import com.hanu.ims.db.SupplierRepositoryImpl;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Supplier;
import com.hanu.ims.model.repository.SupplierRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SupplierController {
    private SupplierRepository repository;

    public SupplierController() {
        repository = ServiceContainer.locateDependency(SupplierRepository.class);
    }

    public boolean createSupplier(Supplier supplier) {
        if (isValidated(supplier)) {
            repository.add(supplier);
            return true;
        }
        return false;
    }

    public boolean invalidateSupplier(Supplier supplier) {
        if (isValidated(supplier)) {
            supplier.setIsAvailable(false);
            repository.save(supplier);
            return true;
        }
        return false;
    }

    public Supplier getSupplierDetails(int id) {
        return repository.findById(id);
    }

    public Supplier updateSupplier(Supplier supplier) {
        return repository.save(supplier);
    }

    public ObservableList<Supplier> getSupplierList()  {
        ObservableList<Supplier> suppliers = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            suppliers.setAll(repository.findAll());
        });
        dbThread.start();
        return suppliers;
    }
    
    //helper

    public boolean isIn(Supplier supplier) {
        if (getSupplierDetails(supplier.getId()) != null) {
            return true;
        }
        return false;
    }

    public boolean isValidated(Supplier supplier) {
        if (supplier != null || !isIn(supplier)) {
            return true;
        } else {
            return false;
        }
    }

    public ObservableList<Batch> getBatchBySupplier(Supplier supplier) {
        ObservableList<Batch> batches = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            batches.setAll(repository.findBatchBySupplier(supplier));
        });
        dbThread.start();
        return batches;
    }
}