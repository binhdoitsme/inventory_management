package com.hanu.ims.controller;

import com.hanu.ims.model.repository.ProductRepository;
import com.hanu.ims.util.servicelocator.ServiceContainer;

public class ProductController {
    private ProductRepository repository;

    public ProductController() {
        repository = ServiceContainer.locateDependency(ProductRepository.class);
    }
}
