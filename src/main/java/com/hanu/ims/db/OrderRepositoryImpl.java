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
import java.util.stream.Collectors;

public class OrderRepositoryImpl extends RepositoryImpl<Order, Integer>
        implements OrderRepository {

    // constants
    private static final String FIND_BY_ID = Configuration.get("db.sql.order.findById");
    private static final String FIND_ALL = Configuration.get("db.sql.order.findAll");
    private static final String FIND_ALL_PRODUCTS = Configuration.get("db.sql.product.findAll");
    private static final String ADD_ORDER_LINES = Configuration.get("db.sql.order.addOrderLines");
    private static final String REMOVE_ORDER_LINES = Configuration.get("db.sql.order.removeOrderLines");
    private static final String ADD_ONE = Configuration.get("db.sql.order.addOne");
    private static final String DELETE_ALL = Configuration.get("db.sql.order.deleteAll");
    private static final String DELETE_BY_ID = Configuration.get("db.sql.order.deleteById");
    // mappers
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
        String sql = ADD_ORDER_LINES;
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
        if (orderLines.isEmpty()) {
            throw new NullPointerException("No order lines to add!");
        }
        int orderId = orderLines.get(0).getOrderId();
        List<String> idList = orderLines.stream()
                .map(item -> String.valueOf(item.getBatchId()))
                .collect(Collectors.toList());
        try {
            String sql = REMOVE_ORDER_LINES;
            int rowsAffected = getConnector().connect()
                    .executeDelete(sql
                            .replace("$id_list", String.join(", ", idList))
                            .replace("$order_id", String.valueOf(orderId)));
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean add(Order item) {
        try {
            Timestamp timestamp = Timestamp.from(Instant.ofEpochSecond(item.getTimestamp()));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestampStr = df.format(timestamp);
            String sql = ADD_ONE.replace("$timestamp", timestampStr)
                            .replace("$cashier_id", String.valueOf(item.getCashierId()));
            return getConnector().connect().executeInsert(sql) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean add(List<Order> items) { return false; }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean delete(Order item) {
        int id = item.getId();
        return deleteById(id);
    }

    @Override
    public boolean deleteAll(List<Order> items) {
        List<String> idList = items.stream()
                .map(item -> String.valueOf(item.getId()))
                .collect(Collectors.toList());
        try {
            String sql = DELETE_ALL;
            return getConnector().connect()
                    .executeDelete(sql.replace("$id_list", String.join(", ", idList))) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean deleteById(Integer integer) {
        try {
            String sql = DELETE_BY_ID;
            return getConnector().connect()
                    .executeDelete(sql.replace("$id", String.valueOf(integer))) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
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
