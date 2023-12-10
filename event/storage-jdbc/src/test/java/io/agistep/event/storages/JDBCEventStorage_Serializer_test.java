package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventSource;
import io.agistep.event.Serializer;
import io.agistep.event.serialization.JsonSerializer;
import io.agistep.event.serialization.ProtocolBufferSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class JDBCEventStorage_Serializer_test {

    JDBCEventStorage eventStorage;
    @BeforeEach
    void setUp() {
        String url = "jdbc:tc:postgresql:9.6.8:///eventStorage?TC_INITSCRIPT=init-event-storage.sql";
        String driverName = "org.testcontainers.jdbc.ContainerDatabaseDriver";
        eventStorage = new JDBCEventStorage(driverName, url);
    }

    @Test
    void supportedSerializer_test() {
        Serializer[] serializers = eventStorage.supportedSerializer();
        Serializer[] expectedSerializers = new Serializer[]{new JsonSerializer(), new ProtocolBufferSerializer()};

        assertThat(serializers).isEqualTo(expectedSerializers);
    }

    @Test
    @DisplayName("Event Payload 는 Serializer을 사용한다")
    void typeTest() {
        JsonSerializer jsonSerializerSpy = Mockito.spy(new JsonSerializer());
        eventStorage.setSerializer(jsonSerializerSpy);
        JsonPayloadTest testAnEventPayload = new JsonPayloadTest("Test anEvent");

        Event anEvent = EventSource.builder()
                .id(2L)
                .seq(0L)
                .aggregateId(1L)
                .payload(testAnEventPayload)
                .occurredAt(LocalDateTime.of(2023,12,12,0,0))
                .build();

        eventStorage.save(anEvent);

         verify(jsonSerializerSpy).serialize(testAnEventPayload);
    }

    private record JsonPayloadTest(String value) {
    }
}
