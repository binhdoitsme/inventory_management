package com.hanu.ims.util.servicelocator;

import com.hanu.ims.exception.DependencyNotInjectedException;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {

    private static ServiceContainer instance;

    private static Map<Class<?>, Object> dependencyContainer = new HashMap<>();

    ServiceContainer() {
    }

    public ServiceContainer addDependency(Class<?> dependencyType, Object implementation) {
        dependencyContainer.put(dependencyType, implementation);
        return this;
    }

    public static ServiceContainer getInstance() {
        if (instance == null) {
            instance = new ServiceContainer();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> T locateDependencyFor(Class<T> dependencyClass) {
        T dependencyResult = (T) dependencyContainer.get(dependencyClass);
        if (dependencyResult == null) {
            throw new DependencyNotInjectedException();
        }
        return dependencyResult;
    }

    public static <T> T locateDependency(Class<T> dependencyClass) {
        return instance.locateDependencyFor(dependencyClass);
    }
}
