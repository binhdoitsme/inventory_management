package com.hanu.ims.controller;

import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.domain.Supplier;
import com.hanu.ims.model.repository.BatchRepository;
import com.hanu.ims.model.repository.OrderRepository;
import com.hanu.ims.model.repository.SupplierRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class BatchController {
    private BatchRepository repository;
    private OrderRepository orderRepository;
    private SupplierRepository supplierRepository;

    public BatchController() {
        repository = ServiceContainer.locateDependency(BatchRepository.class);
        orderRepository = ServiceContainer.locateDependency(OrderRepository.class);
        supplierRepository = ServiceContainer.locateDependency(SupplierRepository.class);
    }

    public ObservableList<Batch> getBatchList() {
        ObservableList<Batch> batchList = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            List<Batch> result = repository.findAll();
            batchList.setAll(result);
        });
        dbThread.start();
        return batchList;
    }

    public void removeBatch(int id) {
        repository.deleteById(id);
    }

    public ObservableList<Category> getCategorySuggestions() {
        ObservableList<Category> result = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            result.setAll(repository.getCategorySuggestions());
        });
        dbThread.start();
        return result;
    }

    public ObservableList<Product> getAllProductSuggestions() {
        ObservableList<Product> productObservableList = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            List<Product> productList = orderRepository.getAllProductSuggestions();
            if (productList == null || productList.isEmpty()) {
                return;
            }
            productObservableList.setAll(productList);
        });
        dbThread.start();
        return productObservableList;
    }

    public ObservableList<Supplier> getAllSupplierSuggestions() {
        ObservableList<Supplier> supplierObservableList = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            List<Supplier> supplierList = supplierRepository.findAllActiveSuppliers();
            if (supplierList == null || supplierList.isEmpty()) {
                return;
            }
            supplierObservableList.setAll(supplierList);
        });
        dbThread.start();
        return supplierObservableList;
    }

    public void addBatch(Batch batch) {
        repository.add(batch);
    }

    public Batch updateBatch(Batch batch) {
        return repository.save(batch);
    }
}
