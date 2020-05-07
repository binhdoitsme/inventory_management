package com.hanu.ims.model.repository;

import com.hanu.ims.base.Repository;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;

import java.util.List;

public interface OrderRepository extends Repository<Order, Integer> {
    boolean addOrderLines(List<OrderLine> orderLines);
}
