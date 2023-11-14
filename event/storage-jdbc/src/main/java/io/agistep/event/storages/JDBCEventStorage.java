package io.agistep.event.storages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agistep.event.Event;
import io.agistep.event.Events;
import org.postgresql.util.PGobject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class JDBCEventStorage extends OptimisticLockingSupport {
    static final String INSERT_DML = "INSERT INTO events" +
            "(id, seq, name, aggregateId, payload, occurredAt)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    static final String SELECT_QUERY = "SELECT id, seq, name, aggregateId, payload, occurredAt FROM events WHERE aggregateId = ?";

    Connection conn = null;

    public JDBCEventStorage() {
        this("jdbc:postgresql://localhost:5422/agistep", "agistep", "agistep", "org.postgresql.Driver");
    }

    public JDBCEventStorage(String driverName, String url, String id, String pw) {
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, id, pw);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public JDBCEventStorage(String driverName, String url) {
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url);
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

            prep.setObject(5, payload.toString());
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

                Object payload = rs.getObject("payload");

                Event anEvent = Events.builder()
                        .id(rs.getLong("id"))
                        .aggregateId(rs.getLong("aggregateId"))
                        .name(rs.getString("name"))
                        .seq(rs.getLong("seq"))
                        .payload(payload) // TODO 어떻께?

                        .occurredAt(timestamp.toLocalDateTime()).build();
                events.add(anEvent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }
}
