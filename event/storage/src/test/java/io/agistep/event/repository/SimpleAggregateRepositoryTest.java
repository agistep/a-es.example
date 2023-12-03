package io.agistep.event.repository;

import io.agistep.event.EventSource;
import io.agistep.event.storages.MapEventStorage;
import io.agistep.foo.Foo;
import io.agistep.foo.FooCreated;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleAggregateRepositoryTest {


    @Test
    void name() {
        MapEventStorage storage = new MapEventStorage();
        SimpleAggregateRepository<Foo> sut = new SimpleAggregateRepository<>(
                Foo::new, storage);

        Foo foo = new Foo();
        FooCreated created = new FooCreated();
        EventSource.apply(foo, created);
        sut.save(foo);

        assertThat(sut.findById(foo.getId()).get().getId())
                .isEqualTo(foo.getId());
    }
}