package io.agistep.event.storages;

import com.zaxxer.hikari.HikariDataSource;
import io.agistep.event.Event;
import io.agistep.event.EventSource;
import io.agistep.event.serializer.ConvertUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class JDBCEventStorage extends OptimisticLockingSupport {
    static final String INSERT_DML = "INSERT INTO events" +
            "(id, seq, name, aggregateId, payload, occurredAt)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    static final String SELECT_QUERY = "SELECT id, seq, name, aggregateId, payload, occurredAt FROM events WHERE aggregateId = ?";

    Connection conn;

    JDBCEventStorage() {
        this("jdbc:postgresql://localhost:5422/agistep", "agistep", "agistep", "org.postgresql.Driver");
    }

    public JDBCEventStorage(String driverName, String url, String id, String pw) {
        try {

            Class.forName(driverName);
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(url);
            ds.setUsername(id);
            ds.setPassword(pw);
            conn = ds.getConnection();
        } catch (ClassNotFoundException | SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public JDBCEventStorage(String driverName, String url) {
        try {
            Class.forName(driverName);
            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(url);
            conn = ds.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void lockedSave(Event anEvent) {
        long id = anEvent.getId();
        long aggregateId = anEvent.getAggregateId();
        String name = anEvent.getName();
        long seq = anEvent.getSeq();
        Object payload = anEvent.getPayload();
        LocalDateTime occurredAt = anEvent.getOccurredAt();

        try {
            PreparedStatement prep = conn.prepareStatement(INSERT_DML);
            prep.setLong(1, id);
            prep.setLong(2, seq);
            prep.setString(3, name);
            prep.setLong(4, aggregateId);
            prep.setObject(5, serializePayload(payload));
            prep.setTimestamp(6, Timestamp.valueOf(occurredAt));
            prep.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> findByAggregate(long id) {
        List<Event> events = new ArrayList<>();
        try {
            PreparedStatement prep = conn.prepareStatement(SELECT_QUERY);
            prep.setLong(1, id);
            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("occurredAt");

                String name = rs.getString("name");
                Object payload1 = deSerializePayload(rs.getObject("payload"), name);

                Event anEvent = EventSource.builder()
                        .id(rs.getLong("id"))
                        .aggregateId(rs.getLong("aggregateId"))
                        .name(name)
                        .seq(rs.getLong("seq"))
                        .payload(payload1)
                        .occurredAt(timestamp.toLocalDateTime()).build();
                events.add(anEvent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    private static String serializePayload(Object payload) {
        return ConvertUtil.serializePayload(payload);
    }

    private Object deSerializePayload(Object payload, String name) {
        return ConvertUtil.deSerializePayload(payload, name);
    }
}
