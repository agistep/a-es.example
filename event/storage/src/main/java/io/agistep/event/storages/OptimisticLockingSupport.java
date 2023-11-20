package io.agistep.event.storages;

import io.agistep.event.Event;

abstract class OptimisticLockingSupport implements EventStorage {
    @Override
    final public void save(Event anEvent) {
        if (this.possible(anEvent.getAggregateId(), anEvent.getSeq())) {
            throw new OptimisticLockedException();
        }

        lockedSave(anEvent);
    }

    protected boolean possible(long aggregateId, long seq) {
        long ltv = findLatestSeqOfAggregate(aggregateId);
        return ltv >= seq;
    }

    public abstract void lockedSave(Event anEvent);

}
