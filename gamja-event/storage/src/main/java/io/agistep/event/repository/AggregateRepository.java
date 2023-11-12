package io.agistep.event.repository;

import java.util.Optional;

public interface AggregateRepository<AGG> {

    long getNextId();

    void save(AGG aggregate);

    Optional<AGG> findById(long id);
}
