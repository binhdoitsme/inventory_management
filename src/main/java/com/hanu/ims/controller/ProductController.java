package com.hanu.ims.controller;

import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.repository.BatchRepository;
import com.hanu.ims.model.repository.ProductRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private ProductRepository productRepository;
    private BatchRepository batchRepository;

    public ProductController() {
        productRepository = ServiceContainer.locateDependency(ProductRepository.class);
        batchRepository = ServiceContainer.locateDependency(BatchRepository.class);
    }

    public ObservableList<Category> getAllCategories() {
        // get all possible categories for combo box
        ObservableList<Category> result = FXCollections.observableList(new ArrayList<>());
        Thread dbThread = new Thread(() -> {
            List<Category> categories = batchRepository.getCategorySuggestions();
            if (categories == null || categories.isEmpty()) {
                return;
            }
            result.setAll(categories);
        });
        dbThread.start();
        return result;
    }

    public void addProduct(Product product) {
        productRepository.add(product);
    }
}
