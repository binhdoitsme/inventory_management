package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.repository.BatchRepository;

import java.util.List;
import java.util.Map;

public class BatchRepositoryImpl extends RepositoryImpl<Batch, Integer>
        implements BatchRepository {
    @Override
    public List<Batch> findBySku(String sku) {
        return null;
    }

    @Override
    public Map<Batch, Integer> getBatchesAndQuantityFromOrderLines(List<OrderLine> orderLines) {
        return null;
    }

    @Override
    public boolean add(Batch item) {
        return false;
    }

    @Override
    public void add(List<Batch> items) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Batch item) {

    }

    @Override
    public void deleteAll(List<Batch> items) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public boolean existById(Integer integer) {
        return false;
    }

    @Override
    public Batch findById(Integer integer) {
        return null;
    }

    @Override
    public List<Batch> findAllById(List<Integer> integers) {
        return null;
    }

    @Override
    public List<Batch> findAll() {
        return null;
    }

    @Override
    public Batch save(Batch item) {
        return null;
    }

    @Override
    public List<Batch> saveAll(List<Batch> items) {
        return null;
    }
}
