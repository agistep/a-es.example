package io.agistep.event.repository;

import io.agistep.event.storages.EventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
@SuppressWarnings("ClassNamingConvention")
class SimpleAggregateRepository_Optimistic_Locking_Test {

    SimpleAggregateRepository<Foo> sut;

    @Mock
    EventStorage storage;

    @BeforeEach
    void setUp() {
        this.sut = new SimpleAggregateRepository<>(Foo::new, storage);
    }



    private static class Foo {
    }
}