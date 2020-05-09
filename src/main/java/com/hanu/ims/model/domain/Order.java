package com.hanu.ims.model.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private final int id;
    private final int cashierId;
    private String cashierName;
    private List<OrderLine> orderLines;
    private long timestamp;

    /**
     * Complete constructor for Read operations
     */
    public Order(int id, int cashierId, String cashierName, List<OrderLine> orderLines, long timestamp) {
        this.id = id;
        this.cashierName = cashierName;
        this.cashierId = cashierId;
        this.orderLines = orderLines;
        this.timestamp = timestamp;
    }

    /**
     * Constructor for a new order
     * @param cashierId
     */
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

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                cashierId == order.cashierId &&
                timestamp == order.timestamp &&
                Objects.equals(cashierName, order.cashierName) &&
                orderLines.equals(order.orderLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cashierId, cashierName, orderLines, timestamp);
    }
}
