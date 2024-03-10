package io.agistep;

import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;

public class FooProviderFactory implements IdentifierProviderFactory {
    @Override
    public IdentifierProvider get() {
        return () -> 101;
    }
}
