package com.hanu.ims.app;

import com.hanu.ims.Startup;
import com.hanu.ims.mock.db.MockSupplierRepositoryImpl;
import com.hanu.ims.model.repository.SupplierRepository;
import com.hanu.ims.util.configuration.Configuration;
import com.hanu.ims.util.db.DbConnector;
import com.hanu.ims.util.db.DbConnectorImpl;
import com.hanu.ims.util.servicelocator.ServiceContainer;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;

public class MockStartUp extends Startup {
    private ServiceContainer container;

    public MockStartUp() {
    }

    @Override
    public Startup configureDependencies()
            throws ClassNotFoundException, IOException, NoSuchMethodException {
        // load dependency injection instances here
        container = ServiceContainer.getInstance();

        // add dependencies here

        container.addDependency(DbConnector.class, new DbConnectorImpl());
        container.addDependency(SupplierRepository.class, new MockSupplierRepositoryImpl());
        return this;
    }

    @Override
    public Startup getConfigurations() throws ConfigurationException {
        Configuration.getInstance().loadConfigurations();
        return this;
    }
}
