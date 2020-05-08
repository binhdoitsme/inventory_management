package com.hanu.ims.base;

import com.hanu.ims.util.db.DbConnector;
import com.hanu.ims.util.db.DbConnectorImpl;
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
}
