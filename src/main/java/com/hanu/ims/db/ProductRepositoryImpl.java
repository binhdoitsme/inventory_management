package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.repository.ProductRepository;

import java.util.List;

public class ProductRepositoryImpl extends RepositoryImpl<Product, Integer>
        implements ProductRepository {
    private static final String ADD_ONE = "INSERT INTO product VALUES ('$sku', '$name', '$desc', '$cat_id')";

    @Override
    public boolean add(Product item) {
        String sql = ADD_ONE.replace("$sku", item.getSku())
                .replace("$name", item.getName())
                .replace("$desc", item.getDescription())
                .replace("$cat_id", String.valueOf(item.getCategory().getId()));
        try {
            int rowsAffected = getConnector().connect().executeInsert(sql);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean add(List<Product> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Product item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteAll(List<Product> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existById(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Product findById(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Product> findAllById(List<Integer> integers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Product> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Product save(Product item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Product> saveAll(List<Product> items) {
        throw new UnsupportedOperationException();
    }
}
