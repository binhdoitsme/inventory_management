package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Order;

import java.sql.ResultSet;

public class OrderMapper extends Mapper<Order> {

    public OrderMapper() {
        super(OrderMapper::fromDatabase);
    }

    private static Order fromDatabase(ResultSet rs) {
        // TODO: replace this stub
        throw new UnsupportedOperationException();
    }
}
