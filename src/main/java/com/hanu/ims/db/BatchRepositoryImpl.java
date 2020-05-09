package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.mapper.BatchMapper;
import com.hanu.ims.model.repository.BatchRepository;
import com.hanu.ims.util.configuration.Configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchRepositoryImpl extends RepositoryImpl<Batch, Integer>
        implements BatchRepository {

    private static final String FIND_AVAILABLE_BY_SKU = Configuration.get("db.sql.batch.findAvailableBySku");
    private final BatchMapper mapper = new BatchMapper();

    @Override
    public List<Batch> findBySku(String sku) {
        return null;
    }

    @Override
    public List<Batch> findAvailableBySku(String sku) {
        try {
            ResultSet rs = getConnector().connect().executeSelect(FIND_AVAILABLE_BY_SKU);
            List<Batch> batches = new ArrayList<>();
            while (rs.next()) {
                batches.add(mapper.forwardConvert(rs));
            }
            return batches;
        } catch (Exception e) {
            throw new DbException(e);
        }
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
