DROP TABLE IF EXISTS events;
create table events
(
    id          bigint    not null unique,
    seq         int          CHECK (seq > -1),
    name        varchar(120) not null,
    aggregateId bigint       not null,
    payload     varchar(10000),
    occurredAt  timestamp
    -- UNIQUE (aggregateId, seq)
);

comment
    on table events is 'event 저장소';

comment
    on column events.id is 'event id ';

comment
    on column events.seq is 'aggregate id 에 event version , optimistic locking 에 이용된다.';

comment
    on column events.name is 'event name, ex) com.kurly.fulfilment.outbound.ToteCreated ';

comment
    on column events.aggregateId is 'event 를 발행시킨 Aggregate Id ';

create index events_idx01 on events (aggregateId);


select max(seq) from events where aggregateId = 3243242;