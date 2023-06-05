package io.agistep.event;

import io.agistep.identity.Identity;

import java.lang.reflect.Field;

final class AggregateSupports {

	static long getId(Object aggregate) {
		try {
			Field field = aggregate.getClass().getDeclaredField("id");
			field.setAccessible(true);
			if (field.get(aggregate) instanceof Identity) {
				Identity<Long> aa = (Identity<Long>) (field.get(aggregate));
				return aa.getValue();
			}
			throw new RuntimeException();
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
