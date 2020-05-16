package com.hanu.ims.model.domain;

import java.util.List;
import java.util.Objects;

public class Product {
    private String sku;
    private String name;
    private String description;
    private Category category;
    private List<Batch> batches;

    public Product(String sku, String name, String description) {
        this.sku = sku;
        this.name = name;
        this.description = description;
    }

    public Product(String sku, String name, String description, Category category, List<Batch> batches) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.batches = batches;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public List<Batch> getBatches() {
        return batches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return sku.equals(product.sku) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                category.equals(product.category) &&
                Objects.equals(batches, product.batches);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("[").append(sku.toUpperCase()).append("] ");
        sb.append(name.toUpperCase());
        return sb.toString();
    }
}
