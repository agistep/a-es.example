package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.ObjectPayloadEnvelop;
import io.agistep.event.sed.Deserializer;
import io.agistep.event.sed.ProtocolBufferDeserializer;
import io.agistep.event.sed.ProtocolBufferSerializer;
import io.agistep.event.sed.Serializer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

class CSVFileEventStorage implements EventStorage {
    final static String[] HEADERS = {"id", "version", "name", "aggregateId", "payload", "occurredAt"};
    public static final int COMMA_ASCII = 44;

    Path path;

    public CSVFileEventStorage(File file) {
        this.path = file.toPath();
    }


    @Override
    public void save(List<Event> events) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(this.path.toFile()), csvFormat)) {
            events.forEach(e->{
                try {
                    Object payload1 = e.getPayload();
                    byte[] serialize = serialize(payload1);
                    encodingWithCommaAscii(serialize);
                    final String serialized = new String(serialize);
                    printer.printRecord(
                            e.getId(),
                            e.getVersion(),
                            e.getName(),
                            e.getAggregateId(),
                            serialized,
                            e.getOccurredAt().format(ISO_LOCAL_DATE_TIME));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void encodingWithCommaAscii(byte[] serialize) {
        for (int i = 0; i < serialize.length; i++) {
            serialize[i] = (byte) (serialize[i]+ COMMA_ASCII);
        }
    }

    private static byte[] serialize(Object payload) {
        Serializer serializer = new ProtocolBufferSerializer();
        if(serializer.isSupport(payload)) {
            return serializer.serialize(payload);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<Event> findAll() {
        Iterable<CSVRecord> records = getCsvRecords();

        List<Event> events = new ArrayList<>();
        for (CSVRecord record : records) {
            Event event = getEvent(record);

            events.add(event);
        }
        return events;
    }

    @Override
    public List<Event> findById(long id) {
        Iterable<CSVRecord> records = getCsvRecords();

        List<Event> events = new ArrayList<>();
        for (CSVRecord record : records) {
            Event event = getEvent(record);
            if(event.getAggregateId() == id) {
                events.add(event);
            }
        }
        return events;
    }

    private static Event getEvent(CSVRecord record) {
        return new ObjectPayloadEnvelop(
                Long.parseLong(record.get("id")),
                record.get("name"),
                Long.parseLong(record.get("version")),
                Long.parseLong(record.get("aggregateId")),
                deserialize(record),
                LocalDateTime.parse(record.get("occurredAt"),DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private static Object deserialize(CSVRecord record) {
        try {
            byte[] byteArray = record.get("payload").getBytes();
            decodingWithCommaAscii(byteArray);
            Class<?> clazz = Class.forName(record.get("name"));
            Deserializer deserializer = new ProtocolBufferDeserializer(clazz);
            return deserializer.deserialize(byteArray);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void decodingWithCommaAscii(byte[] byteArray) {
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) (byteArray[i]- COMMA_ASCII);
        }
    }

    private Iterable<CSVRecord> getCsvRecords() {
        try {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(false)
                    .build();
            return csvFormat.parse (getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Reader getReader() {
        Reader reader;
        try {
            reader = new FileReader(this.path.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return reader;
    }
}
