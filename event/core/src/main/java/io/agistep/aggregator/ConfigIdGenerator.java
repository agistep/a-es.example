package io.agistep.aggregator;

import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;

public class ConfigIdGenerator {

    private final IdentifierProvider identifierProvider;

    public ConfigIdGenerator() {
        // TODO load factoryName from configuration
        // 이 설정은 Todo gamja.yaml 에서 설정 가능
        String factoryName = "io.agistep.FooProviderFactory";

        identifierProvider = IdentifierProviderFactory
                .load(factoryName)
                .getOrDefault();
    }

    public long gen() {
        return identifierProvider.nextId();
    }
}
