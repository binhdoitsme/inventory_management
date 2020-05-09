package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.mapper.OrderListMapper;
import com.hanu.ims.model.repository.OrderRepository;
import com.hanu.ims.util.configuration.Configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl extends RepositoryImpl<Order, Integer>
        implements OrderRepository {

    private static final String FIND_BY_ID = Configuration.get("db.sql.order.findById");
    private static final String FIND_ALL = Configuration.get("db.sql.order.findAll");

    private final OrderListMapper mapper;

    public OrderRepositoryImpl() {
        mapper = new OrderListMapper();
    }

    @Override
    public boolean addOrderLines(List<OrderLine> orderLines) {
        return false;
    }

    @Override
    public boolean removeOrderLines(List<OrderLine> orderLines) {
        return false;
    }

    @Override
    public boolean add(Order item) {
        return false;
    }

    @Override
    public void add(List<Order> items) {

    }

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
}
