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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchRepositoryImpl extends RepositoryImpl<Batch, Integer>
        implements BatchRepository {

    private static final String FIND_AVAILABLE_BY_SKU = Configuration.get("db.sql.batch.findAvailableBySku");
    private static final String FIND_BY_ID = Configuration.get("db.sql.batch.findById");
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
        int orderId = orderLines.get(0).getOrderId();
        String sql = "SELECT *, o.quantity _order_line_qty FROM batch b INNER JOIN _order_line o ON o.batch_id = b.id WHERE _order_id = '$id'".replace("$id", String.valueOf(orderId));
        Map<Batch, Integer> batches = new HashMap<>();
        try {
            ResultSet rs = getConnector().connect().executeSelect(sql);
            while (rs.next()) {
                int quantity = rs.getInt("_order_line_qty");
                batches.put(mapper.forwardConvert(rs), quantity);
            }
            return batches;
        } catch (Exception e) {
            throw new DbException(e);
        }
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
        String sql = FIND_BY_ID.replace("$id", integer.toString());
        try {
            ResultSet rs = getConnector().connect().executeSelect(sql);
            rs.next();
            return mapper.forwardConvert(rs);
        } catch (Exception e) {
            throw new DbException(e);
        }
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
        String template = "UPDATE batch SET quantity='$qty', import_price='$importPrice', msrp='$msrp' WHERE id='$id'";
        String sql = template.replace("$qty", String.valueOf(item.getQuantity()))
                .replace("$importPrice", String.valueOf(item.getImportPrice()))
                .replace("$msrp", String.valueOf(item.getRetailPrice()))
                .replace("$id", String.valueOf(item.getId()));
        try {
            boolean updated = getConnector().connect().executeUpdate(sql) > 0;
            if (updated) return findById(item.getId());
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public List<Batch> saveAll(List<Batch> items) {
        List<Batch> savedBatches = new ArrayList<>();
        for (Batch item : items) {
            savedBatches.add(save(item));
        }
        return savedBatches;
    }
}
