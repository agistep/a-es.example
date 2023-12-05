package io.agistep.aggregator;


import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

import static java.lang.String.format;

public final class IdUtils {

	private static final IdentifierProvider IDENTIFIER_PROVIDER = IdentifierProviderFactory.load().get();
	private static final Class<?>[] SUPPORTED_TYPES = { long.class, Long.class };
	private static final String NAME_OF_ID_FIELD = "id";

	public static long idOf(Object aggregate) {

		Field field = getIdField(aggregate);

		try{

			Object id = field.get(aggregate);
			if(field.getType().isPrimitive() &&  Long.valueOf(0).equals(id)) {
				throw new IllegalAggregateIdException(format("Primitive Type lnt and long must not have 0(zero). :%s", aggregate.getClass().getName()));
			}

			if(!field.getType().isPrimitive() && id == null)  {
				throw new IllegalAggregateIdException(format("An Id must not be null. :%s", aggregate.getClass().getName()));
			}

			return (Long) id;
		} catch (IllegalAccessException e) {
			throw new IllegalAggregateIdException(e.getMessage(), e);
		}
	}

	private static Field getIdField(Object aggregate) {
		Field field;
		try {
			field = aggregate.getClass().getDeclaredField(NAME_OF_ID_FIELD);
		} catch (NoSuchFieldException e) {
			throw new IllegalAggregateIdException(
					format("Aggregate Must Have 'id' field. :%s", aggregate.getClass().getName()),e);
		}

		if(isNotSupport(field)) {
			throw new IllegalAggregateIdException(
					format("An ID field is applied should be one of the following types: " +
					"long, Long. :%s", aggregate.getClass().getName()),null);
		}

		field.setAccessible(true);
		return field;
	}

	private static boolean isNotSupport(Field field) {
		return Arrays.stream(SUPPORTED_TYPES).noneMatch(t -> t.getTypeName().equals(field.getType().getTypeName()));
	}

	public static boolean notAssignedIdOf(Object aggregate) {
		Field field = getIdField(aggregate);
		try {
			Object id = field.get(aggregate);
			if (field.getType().isPrimitive() && ((Number) id).longValue() == 0L) {
				return true;
			}

			return !field.getType().isPrimitive() && id == null;
		} catch (IllegalAccessException e) {
			throw new IllegalAggregateIdException(e.getMessage(), e);
		}
	}

	public static long gen() {
		return IDENTIFIER_PROVIDER.nextId();
	}
}

