package io.agistep.event.storages;

import io.agistep.event.Event;

abstract class OptimisticLockingSupport implements EventStorage {
    @Override
    final public void save(Event anEvent) {
        if(this.possible(anEvent.getAggregateId(), anEvent.getVersion())){
            throw new OptimisticLockedException();
        }

        lockedSave(anEvent);
    }

    protected boolean possible(long aggregateId, long version) {
        long ltv = findLatestVersionOfAggregate(aggregateId);
        return ltv >= version;
    }

    public abstract void lockedSave(Event anEvent);

}
