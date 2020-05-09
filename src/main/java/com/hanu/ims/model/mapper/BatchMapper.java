package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Batch;

import java.sql.ResultSet;

public class BatchMapper extends Mapper<Batch> {
    public BatchMapper() {
        super(BatchMapper::fromDatabase);
    }

    private static Batch fromDatabase(ResultSet rs) {
        try {
            return new Batch(rs.getInt("id"),
                    rs.getString("sku"),
                    rs.getInt("quantity"),
                    rs.getDate("import_date"),
                    rs.getLong("msrp"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
