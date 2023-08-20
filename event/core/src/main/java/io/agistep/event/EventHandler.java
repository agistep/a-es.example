package io.agistep.event;

import java.lang.annotation.*;


@Documented
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventHandler {

    Class<?> payload();
}
