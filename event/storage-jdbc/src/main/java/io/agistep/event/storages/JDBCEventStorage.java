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

class JDBCEventStorage implements EventStorage {
    Connection conn = null;

    public JDBCEventStorage() {
        String url = "jdbc:postgresql://localhost:5422/agistep";
        String id = "agistep";
        String pw = "agistep";

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, id, pw);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Event anEvent) {
        long id = anEvent.getId();
        long aggregateId = anEvent.getAggregateId();
        String name = anEvent.getName();
        long seq = anEvent.getSeq();
        Object payload = anEvent.getPayload();
        LocalDateTime occurredAt = anEvent.getOccurredAt();


        try {
            PreparedStatement prep = conn.prepareStatement("INSERT INTO events VALUES (?, ?, ?, ?, ?, ?)");
            prep.setLong(1, id);
            prep.setLong(2, aggregateId);
            prep.setString(3, name);
            prep.setLong(4, seq);

            ObjectMapper o = new ObjectMapper();
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(o.writeValueAsString(payload));

            prep.setObject(5, jsonObject);
            prep.setTimestamp(6, Timestamp.valueOf(occurredAt));
            prep.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> findByAggregate(long id) {
        List<Event> events = new ArrayList<>();
        try {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM events WHERE aggregate_id = ?");
            prep.setLong(1, id);
            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(6);

                Event anEvent = Events.builder()
                        .id(rs.getLong(1))
                        .aggregateId(rs.getLong(2))
                        .name(rs.getString(3))
                        .seq(rs.getLong(4))
                        .payload(rs.getString(5))
                        .occurredAt(timestamp.toLocalDateTime()).build();
                events.add(anEvent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }
}
