package io.agistep.event.storages;

import com.zaxxer.hikari.HikariDataSource;
import io.agistep.event.*;
import io.agistep.event.serialization.JsonObjectDeserializer;
import io.agistep.event.serialization.JsonSerializer;
import io.agistep.event.serialization.ProtocolBufferDeserializer;
import io.agistep.event.serialization.ProtocolBufferSerializer;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class JDBCEventStorage extends OptimisticLockingSupport {
    static final String INSERT_DML = "INSERT INTO events" +
            "(id, seq, name, aggregateId, payload, occurredAt)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    static final String SELECT_QUERY = "SELECT id, seq, name, aggregateId, payload, occurredAt FROM events WHERE aggregateId = ?";

    Connection conn;
    Serializer serializer;

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

        String s;
        if (Objects.isNull(serializer)) {
            Serializer serializer = Arrays.stream(supportedSerializer())
                    .filter(ser -> ser.isSupport(payload))
                    .findFirst()
                    .orElseThrow(UnsupportedOperationException::new);
            s = new String(serializer.serialize(payload));
        } else {
            s = new String(serializer.serialize(payload));
        }

        try {
            PreparedStatement prep = conn.prepareStatement(INSERT_DML);
            prep.setLong(1, id);
            prep.setLong(2, seq);
            prep.setString(3, name);
            prep.setLong(4, aggregateId);
            //TODO 여기에 Serilizer 가 추가되어야 한다.
            prep.setObject(5, s);
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
                Class<?> clazz;
                try {
                    clazz = Class.forName(name);

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Object payload = rs.getObject("payload");
                Deserializer[] deserializers = supportedDeSerializer(clazz);

                Deserializer deserializer = Arrays.stream(deserializers)
                        .filter(ser -> ser.isSupport(payload))
                        .findFirst()
                        .orElseThrow(UnsupportedOperationException::new);

                Object deserialize = deserializer.deserialize(String.valueOf(payload).getBytes(StandardCharsets.UTF_8));
                Event anEvent = EventSource.builder()
                        .id(rs.getLong("id"))
                        .aggregateId(rs.getLong("aggregateId"))
                        .seq(rs.getLong("seq"))
                        .payload(deserialize)
                        .occurredAt(timestamp.toLocalDateTime()).build();
                events.add(anEvent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    @Override
    public Serializer[] supportedSerializer() {
        return new Serializer[]{
                new JsonSerializer(),
                new ProtocolBufferSerializer()
        };
    }

    @Override
    public Deserializer[] supportedDeSerializer(Class<?> name) {
        return new Deserializer[]{
                new JsonObjectDeserializer(name),
                new ProtocolBufferDeserializer(name)
        };
    }

    void setSerializer(JsonSerializer serializer) {
        this.serializer = serializer;
    }
}
