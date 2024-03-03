package io.agistep.aggregator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.agistep.aggregator.IdAssignStrategy.AUTO;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AggregateId {
    IdAssignStrategy assignStrategy() default AUTO;
}
