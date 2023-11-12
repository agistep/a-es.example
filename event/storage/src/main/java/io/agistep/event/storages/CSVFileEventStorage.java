package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.Events;
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

class CSVFileEventStorage extends OptimisticLockingSupport {
    final static String[] HEADERS = {"id", "version", "name", "aggregateId", "payload", "occurredAt"};
    public static final int COMMA_ASCII = 44;

    Path path;

    public CSVFileEventStorage(File file) {
        this.path = file.toPath();
    }


    private static CSVFormat getCsvFormat() {
        return CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();
    }

    @Override
    public void lockedSave(Event anEvent) {
        CSVFormat csvFormat = getCsvFormat();

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(this.path.toFile()), csvFormat)) {
            save(anEvent, printer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void save(Event anEvent, CSVPrinter printer) {
        try {
            Object payload1 = anEvent.getPayload();
            byte[] serialize = serialize(payload1);
            encodingWithCommaAscii(serialize);
            final String serialized = new String(serialize);
            printer.printRecord(
                    anEvent.getId(),
                    anEvent.getSeq(),
                    anEvent.getName(),
                    anEvent.getAggregateId(),
                    serialized,
                    anEvent.getOccurredAt().format(ISO_LOCAL_DATE_TIME));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void encodingWithCommaAscii(byte[] serialize) {
        for (int i = 0; i < serialize.length; i++) {
            serialize[i] = (byte) (serialize[i] + COMMA_ASCII);
        }
    }

    private static byte[] serialize(Object payload) {
        Serializer serializer = new ProtocolBufferSerializer();
        if (serializer.isSupport(payload)) {
            return serializer.serialize(payload);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<Event> findByAggregate(long id) {
        Iterable<CSVRecord> records = getCsvRecords();

        List<Event> events = new ArrayList<>();
        for (CSVRecord record : records) {
            Event event = getEvent(record);
            if (event.getAggregateId() == id) {
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public long findLatestVersionOfAggregate(long id) {
        return 0;
    }

    private static Event getEvent(CSVRecord record) {
        return Events.builder()
                .id(Long.parseLong(record.get("id")))
                .name(record.get("name"))
                .seq(Long.parseLong(record.get("version")))
                .aggregateId(Long.parseLong(record.get("aggregateId")))
                .payload(deserialize(record))
                .occurredAt(LocalDateTime.parse(record.get("occurredAt"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build();
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
            byteArray[i] = (byte) (byteArray[i] - COMMA_ASCII);
        }
    }

    private Iterable<CSVRecord> getCsvRecords() {
        try {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(false)
                    .build();
            return csvFormat.parse(getReader());
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
