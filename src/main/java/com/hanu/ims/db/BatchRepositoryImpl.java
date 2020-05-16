package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.mapper.BatchMapper;
import com.hanu.ims.model.mapper.CategoryMapper;
import com.hanu.ims.model.mapper.ProductWithoutBatchesMapper;
import com.hanu.ims.model.repository.BatchRepository;
import com.hanu.ims.util.configuration.Configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchRepositoryImpl extends RepositoryImpl<Batch, Integer>
        implements BatchRepository {

    // constants
    private static final String FIND_AVAILABLE_BY_SKU = Configuration.get("db.sql.batch.findAvailableBySku");
    private static final String FIND_BY_ID = Configuration.get("db.sql.batch.findById");
    private static final String FIND_ALL = Configuration.get("db.sql.batch.findAll");
    private static final String GET_BATCHES_AND_QUANTITY_FROM_ORDER_LINE = Configuration.get("db.sql.batch.getBatchesAndQuantityFromOrderLines");
    private static final String GET_CATEGORY_SUGGESTIONS = Configuration.get("db.sql.category.getCategorySuggestions");
    private static final String SAVE = Configuration.get("db.sql.batch.save");

    // mappers
    private final BatchMapper batchMapper = new BatchMapper();
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final ProductWithoutBatchesMapper productMapper = new ProductWithoutBatchesMapper();;

    @Override
    public List<Batch> findBySku(String sku) {
        return null;
    }

    @Override
    public List<Batch> findAvailableBySku(String sku) {
        try {
            ResultSet rs = getConnector().connect().executeSelect(FIND_AVAILABLE_BY_SKU.replace("$sku", sku));
            List<Batch> batches = new ArrayList<>();
            while (rs.next()) {
                batches.add(batchMapper.forwardConvert(rs));
            }
            return batches;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public Map<Batch, Integer> getBatchesAndQuantityFromOrderLines(List<OrderLine> orderLines) {
        int orderId = orderLines.get(0).getOrderId();
        String sql = GET_BATCHES_AND_QUANTITY_FROM_ORDER_LINE.replace("$id", String.valueOf(orderId));
        Map<Batch, Integer> batches = new HashMap<>();
        try {
            ResultSet rs = getConnector().connect().executeSelect(sql);
            while (rs.next()) {
                int quantity = rs.getInt("_order_line_qty");
                batches.put(batchMapper.forwardConvert(rs), quantity);
            }
            return batches;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public List<Category> getCategorySuggestions() {
        String sql = GET_CATEGORY_SUGGESTIONS;
        try {
            ResultSet rs = getConnector().connect().executeSelect(sql);
            Map<Category, List<Product>> categoryProductMap = new HashMap<>();
            while (rs.next()) {
                var currentCategory = categoryMapper.forwardConvert(rs);
                categoryProductMap.putIfAbsent(currentCategory, new ArrayList<>());
                categoryProductMap.get(currentCategory).add(productMapper.forwardConvert(rs));
            }
            List<Category> categories = new ArrayList<>();
            categories.addAll(categoryProductMap.keySet());
            for (Category category : categories) {
                category.getProducts().addAll(categoryProductMap.get(category));
            }
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean add(Batch item) {
        String sql = "INSERT INTO batch (sku, import_quantity, quantity, import_price, msrp, import_date, supplier_id) VALUES ('$sku', '$import_quantity', '$quantity', '$import_price', '$msrp', '$import_date', '$supplier_id')"
                .replace("$sku", item.getSku())
                .replace("$import_quantity", String.valueOf(item.getImportQuantity()))
                .replace("$quantity", String.valueOf(item.getQuantity()))
                .replace("$import_price", String.valueOf(item.getImportPrice()))
                .replace("$msrp", String.valueOf(item.getRetailPrice()))
                .replace("$import_date", item.getImportDate().toString())
                .replace("$supplier_id", String.valueOf(item.getSupplierId()));
        try {
            int rowsAffected = getConnector().connect().executeInsert(sql);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean add(List<Batch> items) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean delete(Batch item) {
        return false;
    }

    @Override
    public boolean deleteAll(List<Batch> items) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean deleteById(Integer integer) {
        return false;
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
            return batchMapper.forwardConvert(rs);
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
        try {
            ResultSet rs = getConnector().connect().executeSelect(FIND_ALL);
            List<Batch> batches = new ArrayList<>();
            while (rs.next()) {
                batches.add(batchMapper.forwardConvert(rs));
            }
            return batches;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public Batch save(Batch item) {
        String template = SAVE;
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
