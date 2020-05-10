package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.mapper.OrderListMapper;
import com.hanu.ims.model.mapper.ProductWithoutBatchesMapper;
import com.hanu.ims.model.repository.OrderRepository;
import com.hanu.ims.util.configuration.Configuration;
import javafx.scene.Scene;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl extends RepositoryImpl<Order, Integer>
        implements OrderRepository {

    private static final String FIND_BY_ID = Configuration.get("db.sql.order.findById");
    private static final String FIND_ALL = Configuration.get("db.sql.order.findAll");
    private static final String FIND_ALL_PRODUCTS = Configuration.get("db.sql.product.findAll");

    private final OrderListMapper mapper;
    private final ProductWithoutBatchesMapper productMapper;

    public OrderRepositoryImpl() {
        mapper = new OrderListMapper();
        productMapper = new ProductWithoutBatchesMapper();
    }

    @Override
    public boolean addOrderLines(List<OrderLine> orderLines) {
        if (orderLines.isEmpty()) {
            throw new NullPointerException("No order lines to add!");
        }
        // build sql
        String sql = "INSERT INTO _order_line VALUES ";
        final String template = "('$quantity', '$batch_id', '$_order_id')";
        List<String> valueStrings = new ArrayList<>();
        orderLines.forEach(orderLine -> {
            int quantity = orderLine.getQuantity();
            int batchId = orderLine.getBatchId();
            int orderId = orderLine.getOrderId();
            valueStrings.add(template.replace("$quantity", String.valueOf(quantity))
                                    .replace("$batch_id", String.valueOf(batchId))
                                    .replace("$_order_id", String.valueOf(orderId)));
        });
        sql = sql.concat(String.join(", ", valueStrings));

        // execute sql
        try {
            int rowsAffected = getConnector().connect().executeInsert(sql);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public boolean removeOrderLines(List<OrderLine> orderLines) {
        return false;
    }

    @Override
    public boolean add(Order item) {
        try {
            Timestamp timestamp = Timestamp.from(Instant.ofEpochSecond(item.getTimestamp()));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestampStr = df.format(timestamp);
            String sql = "INSERT INTO _order VALUES ('0', '$timestamp', '$cashier_id')"
                            .replace("$timestamp", timestampStr)
                            .replace("$cashier_id", String.valueOf(item.getCashierId()));
            return getConnector().connect().executeInsert(sql) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public void add(List<Order> items) { }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Order item) {

    }

    @Override
    public void deleteAll(List<Order> items) {

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
    public Order findById(Integer id) {
        String sql = FIND_BY_ID.replace("$id", id.toString());
        try {
            ResultSet rs = getConnector().connect().executeSelect(sql);
            List<Order> result = mapper.forwardConvert(rs);
            if (result.isEmpty()) throw new NullPointerException();
            return result.get(0);
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public List<Order> findAllById(List<Integer> integers) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        try {
            ResultSet rs = getConnector().connect().executeSelect(FIND_ALL);
            List<Order> orderList = new ArrayList<>();
            orderList.addAll(mapper.forwardConvert(rs));
            return orderList;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public Order save(Order item) {
        return null;
    }

    @Override
    public List<Order> saveAll(List<Order> items) {
        return null;
    }

    @Override
    public List<Product> getAllProductSuggestions() {
        try {
            ResultSet rs = getConnector().connect().executeSelect(FIND_ALL_PRODUCTS);
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(productMapper.forwardConvert(rs));
            }
            return productList;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }
}
