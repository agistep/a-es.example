package io.agistep.event.repository;

@FunctionalInterface
public interface AggregateInitializer<AGG> {

    AGG initAgg();
}
