package com.hanu.ims.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    boolean add(T item);
    boolean add(List<T> items);
    long count();
    boolean delete(T item);
    boolean deleteAll(List<T> items);
    boolean deleteAll();
    boolean deleteById(ID id);
    boolean existById(ID id);
    T findById(ID id);
    List<T> findAllById(List<ID> ids);
    List<T> findAll();
    T save(T item);
    List<T> saveAll(List<T> items);
}
