package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductWithoutBatchesMapper extends Mapper<Product> {
    public ProductWithoutBatchesMapper() {
        super(ProductWithoutBatchesMapper::fromDatabase);
    }

    private static Product fromDatabase(ResultSet rs) {
        try {
            return new Product(rs.getString("sku"),
                    rs.getString("product_name"),
                    rs.getString("description"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
