package io.agistep.identity.snowflake;

import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ClassNamingConvention")
class Snowflake_Identifier_ServiceLoader_Test {

    @Test
    void load() {

        IdentifierProvider identifierProvider = IdentifierProviderFactory.load(SnowflakeIdentifierProviderFactory.class.getName()).get();

        System.out.println(identifierProvider.nextId());


    }

}
