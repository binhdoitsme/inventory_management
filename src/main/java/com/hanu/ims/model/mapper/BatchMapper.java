package com.hanu.ims.model.mapper;

import com.hanu.ims.base.Mapper;
import com.hanu.ims.model.domain.Batch;

import java.sql.ResultSet;
import java.time.Instant;

public class BatchMapper extends Mapper<Batch> {
    private static final long EXPIRATION_DURATION = 7776000;

    public BatchMapper() {
        super(BatchMapper::fromDatabase);
    }

    private static Batch fromDatabase(ResultSet rs) {
        try {
            Batch batch =  new Batch(rs.getInt("id"),
                                    rs.getString("sku"),
                                    rs.getInt("import_quantity"),
                                    rs.getInt("quantity"),
                                    rs.getDate("import_date"),
                                    rs.getLong("import_price"),
                                    rs.getLong("msrp"),
                                    rs.getString("product_name"));
            Object orderId = rs.getObject("_order_id");
            long importDate = batch.getImportDate().getTime() / 1000;
            long dateToday = Instant.now().getEpochSecond();
            long difference = dateToday - importDate;
            int quantity = batch.getQuantity();
            if (quantity == 0) {
                batch.setStatus(Batch.Status.OUT_OF_STOCK);
            } else if (difference > EXPIRATION_DURATION) {
                batch.setStatus(Batch.Status.EXPIRED);
            } else if (orderId == null) {
                batch.setStatus(Batch.Status.IMPORTED);
            } else {
                batch.setStatus(Batch.Status.ORDERED);
            }
            return batch;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
