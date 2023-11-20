package io.agistep.identity.snowflake;

import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;

public class SnowflakeIdentifierProviderFactory implements IdentifierProviderFactory {

    private static final SnowflakeIdentifier IDENTIFIER = new SnowflakeIdentifier();

    public SnowflakeIdentifierProviderFactory() {
    }

    @Override
    public IdentifierProvider get() {
        return IDENTIFIER;
    }
}
