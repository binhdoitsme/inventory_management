package com.hanu.ims.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, ID extends Serializable> {
    void beginTransaction();
    void finishTransaction(boolean hasError);
    boolean add(T item);
    void add(List<T> items);
    long count();
    void delete(T item);
    void deleteAll(List<T> items);
    void deleteAll();
    void deleteById(ID id);
    boolean existById(ID id);
    T findById(ID id);
    List<T> findAllById(List<ID> ids);
    List<T> findAll();
    T save(T item);
    List<T> saveAll(List<T> items);
}
