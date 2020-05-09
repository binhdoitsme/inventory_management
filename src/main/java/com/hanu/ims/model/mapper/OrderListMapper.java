package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.exception.DbException;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListMapper extends Mapper<List<Order>> {

    public OrderListMapper() {
        super(OrderListMapper::fromDatabase);
    }

    private static List<Order> fromDatabase(ResultSet rs) {
        Map<Integer, List<OrderLine>> orderLineMap = new HashMap<>();
        List<Order> orderList = new ArrayList<>();
        try {
            while (rs.next()) {
                // work with order
                int orderId = rs.getInt("order_id");
                if (orderList.stream().anyMatch(order -> order.getId() == orderId)) continue;
                int cashierId = rs.getInt("cashier_id");
                String cashierName = rs.getString("cashier_name");
                long timestamp = rs.getTimestamp("timestamp").getTime();
                Order order = new Order(orderId, cashierId, cashierName, new ArrayList<>(), timestamp);
                orderList.add(order);

                // work with order line
                String sku = rs.getString("sku");
                String productName = rs.getString("product_name");
                long listPrice = rs.getLong("msrp");
                int quantity = rs.getInt("quantity");
                OrderLine orderLine = new OrderLine(sku, productName, listPrice, quantity);
                orderLineMap.putIfAbsent(orderId, new ArrayList<>());
                orderLineMap.get(orderId).add(orderLine);
            }

            // add order lines to orders
            for (Order order : orderList) {
                int id = order.getId();
                order.addOrderLines(orderLineMap.get(id));
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
        return orderList;
    }
}
