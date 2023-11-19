create table events
(
    id          bigint       not null unique,
    seq         int          not null,
    name        varchar(120) not null,
    aggregateId bigint       not null,
    payload     varchar(10000),
    occurredAt  timestamp
);

comment
on table events is 'event 저장소';

comment
on column events.id is 'event id ';

comment
on column events.seq is 'aggregate id 에 event seq , optimistic locking 에 이용된다.';

comment
on column events.name is 'event name, ex) com.kurly.fulfilment.outbound.ToteCreated ';

comment
on column events.aggregateId is 'event 를 발행시킨 Aggregate Id ';

create index events_idx01 on events (aggregateId);