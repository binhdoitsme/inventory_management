package com.hanu.ims.model.repository;

import com.hanu.ims.base.Repository;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.OrderLine;

import java.util.List;
import java.util.Map;

public interface BatchRepository extends Repository<Batch, Integer> {
    List<Batch> findBySku(String sku);
    Map<Batch, Integer> getBatchesAndQuantityFromOrderLines(List<OrderLine> orderLines);
}
