package com.hanu.ims.controller;

import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.repository.BatchRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class BatchController {
    private BatchRepository repository;

    public BatchController() {
        repository = ServiceContainer.locateDependency(BatchRepository.class);
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
}
