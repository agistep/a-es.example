package io.agistep.event;

import io.agistep.aggregator.AggregateId;
import io.agistep.aggregator.IdUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static io.agistep.aggregator.IdAssignStrategy.MANUAL;
import static org.valid4j.Validation.validate;

class IdAssignor {

    static void assignIdByStrategy(Object aggregate, Event createdEvent) {
        validate(createdEvent.getSeq() == 0, IllegalArgumentException.class);

        if (manualIdStrategy(aggregate)) {
            return;
        }

        var idField = IdUtils.getIdField(aggregate.getClass());
        idField.setAccessible(true);
        try {
            idField.set(aggregate, createdEvent.getAggregateId());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean manualIdStrategy(Object aggregate) {
        var field = findIdAnnotatedField(aggregate.getClass());

        if (field.isEmpty()) {
            return false;
        }

        var annotation = field.get().getAnnotation(AggregateId.class);
        return MANUAL.equals(annotation.assignStrategy());
    }

    private static Optional<Field> findIdAnnotatedField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(AggregateId.class))
                .findFirst();
    }
}
