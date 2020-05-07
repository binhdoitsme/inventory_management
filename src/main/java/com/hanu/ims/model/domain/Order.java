package com.hanu.ims.model.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private final int id;
    private final int cashierId;
    private List<OrderLine> orderLines;
    private long timestamp;

    public Order(int id, int cashierId, List<OrderLine> orderLines, long timestamp) {
        this.id = id;
        this.cashierId = cashierId;
        this.orderLines = orderLines;
        this.timestamp = timestamp;
    }

    public Order(int cashierId) {
        this.cashierId = cashierId;
        this.id = 0;
        this.orderLines = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getCashierId() {
        return cashierId;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTotalPrice() {
        return orderLines.stream().map(ol -> ol.getLineSum()).reduce((ol1, ol2) -> ol1 + ol2).get();
    }

    public void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
    }

    public void addOrderLines(List<OrderLine> orderLine) {
        this.orderLines.addAll(orderLine);
    }

    public void setTimestampAsCurrent() {
        this.timestamp = new Date().getTime();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", cashierId=").append(cashierId);
        sb.append(", orderLines=").append(orderLines);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
