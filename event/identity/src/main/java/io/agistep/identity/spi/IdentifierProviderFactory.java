package io.agistep.identity.spi;

import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.IdentifierProviderFactoryNotFoundException;

import java.util.ServiceLoader;

public interface IdentifierProviderFactory {

    String DEFAULT_PROVIDER = "io.agistep.identity.snowflake.SnowflakeIdentifierProviderFactory";

    static IdentifierProviderFactory load() {
        return load(DEFAULT_PROVIDER);
    }
    static IdentifierProviderFactory load(String factoryName) {
        ServiceLoader<IdentifierProviderFactory> loader = ServiceLoader.load(IdentifierProviderFactory.class);
        for (IdentifierProviderFactory factory : loader) {
            if (factoryName.equals(factory.getClass().getName())) {
                return factory;
            }
        }
        throw new IdentifierProviderFactoryNotFoundException("IdentifierProvider Factory " + factoryName + " not found");
    }

    IdentifierProvider get();
}
