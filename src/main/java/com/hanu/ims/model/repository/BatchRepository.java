package com.hanu.ims.model.repository;

import com.hanu.ims.base.Repository;
import com.hanu.ims.model.domain.Batch;

import java.util.List;

public interface BatchRepository extends Repository<Batch, Integer> {
    List<Batch> findBySku(String sku);
}
