package com.hanu.ims.db;

import com.hanu.ims.base.RepositoryImpl;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.mapper.OrderMapper;
import com.hanu.ims.model.repository.OrderRepository;
import com.hanu.ims.util.configuration.Configuration;

import java.sql.ResultSet;
import java.util.List;

public class OrderRepositoryImpl extends RepositoryImpl<Order, Integer>
    implements OrderRepository {

    private static final String FIND_BY_ID = Configuration.get("db.sql.order.findById");
    private static final String FIND_ALL = Configuration.get("db.sql.order.findAll");

    private final OrderMapper mapper;

    public OrderRepositoryImpl() {
        mapper = new OrderMapper();
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
            return mapper.forwardConvert(rs);
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
        return null;
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
