package com.hanu.ims;

import com.hanu.ims.db.*;
import com.hanu.ims.model.repository.*;
import com.hanu.ims.util.configuration.Configuration;
import com.hanu.ims.util.db.DbConnector;
import com.hanu.ims.util.db.DbConnectorImpl;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;

public class Startup {
    private ServiceContainer container;

    public Startup() {
    }

    public Startup configureDependencies()
            throws ClassNotFoundException, IOException, NoSuchMethodException {
        // load dependency injection instances here
        container = ServiceContainer.getInstance();

        // add dependencies here

        container.addDependency(DbConnector.class, new DbConnectorImpl());
        container.addDependency(AccountRepository.class, new AccountRepositoryImpl());
        container.addDependency(OrderRepository.class, new OrderRepositoryImpl());
        container.addDependency(BatchRepository.class, new BatchRepositoryImpl());
        container.addDependency(SupplierRepository.class, new SupplierRepositoryImpl());
        container.addDependency(ProductRepository.class, new ProductRepositoryImpl());
        return this;
    }

    public Startup getConfigurations() throws ConfigurationException {
        Configuration.getInstance().loadConfigurations();
        return this;
    }
}
