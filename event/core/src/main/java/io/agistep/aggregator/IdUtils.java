package io.agistep.aggregator;


import java.lang.reflect.Field;
import java.util.Arrays;

public final class IdUtils {

	public static final Class<?>[] SUPPORTED_TYPES = { long.class, Long.class };

	public static final String NAME_OF_ID_FIELD = "id";

	public static long idOf(Object aggregate) {

		Field field = getIdField(aggregate);

		try{

			Object id = field.get(aggregate);
			if(field.getType().isPrimitive() &&  Long.valueOf(0).equals(id)) {
				throw new IllegalAggregateIdException("Primitive Type lnt and long must not have 0(zero).");
			}

			if(!field.getType().isPrimitive() && id == null)  {
				throw new IllegalAggregateIdException("An Id must not be null.");
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
			throw new IllegalAggregateIdException("Aggregate Must Have 'id' field.",e);
		}

		if(isNotSupport(field)) {
			throw new IllegalAggregateIdException("An ID field is applied should be one of the following types: int, long, Long, Integer, String",null);
		}

		field.setAccessible(true);
		return field;
	}

	private static boolean isNotSupport(Field field) {
		return Arrays.stream(SUPPORTED_TYPES).noneMatch(t -> t.getTypeName().equals(field.getType().getTypeName()));
	}

	public static boolean assertThatNotAssignIdOf(Object aggregate) {
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
		return 11111L; //TODO
	}
}
