package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper extends Mapper<Category> {
    public CategoryMapper() {
        super(CategoryMapper::fromDatabase);
    }

    private static Category fromDatabase(ResultSet rs) {
        try {
            return new Category(rs.getInt("cat_id"), rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
