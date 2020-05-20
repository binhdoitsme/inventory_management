package com.hanu.ims.base;

import com.hanu.ims.exception.DbException;
import com.hanu.ims.util.db.DbConnector;
import com.hanu.ims.util.servicelocator.ServiceContainer;

import java.io.Serializable;

public abstract class RepositoryImpl<T, ID extends Serializable> implements Repository<T, ID> {
    private DbConnector connector;

    public RepositoryImpl() {
        connector = ServiceContainer.locateDependency(DbConnector.class);
    }

    protected DbConnector getConnector() {
        return connector;
    }

    @Override
    public void beginTransaction() {
        try {
            connector.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }

    @Override
    public void finishTransaction(boolean hasError) {
        try {
            if (hasError) connector.rollback();
            else connector.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException(e);
        }
    }
}
