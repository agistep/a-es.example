package io.agistep.aggregator;


import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static java.lang.String.format;

public final class IdUtils {

	private static final IdentifierProvider IDENTIFIER_PROVIDER = IdentifierProviderFactory.load().get();
	private static final Class<?>[] SUPPORTED_TYPES = { long.class };
	private static final String NAME_OF_ID_FIELD = "id";

	public static long idOf(Object aggregate) {

		Field field = getIdField(aggregate.getClass());

		try{
			Object id = field.get(aggregate);
			if (field.getType().isPrimitive() &&  Long.valueOf(0).equals(id)) {
				throw new IllegalAggregateIdException(format("An Aggregate id must not be 0(zero). :%s", aggregate.getClass().getName()));
			}

			return (long) id;
		} catch (IllegalAccessException e) {
			throw new IllegalAggregateIdException(e.getMessage(), e);
		}
	}

	public static Field getIdField(Class<?> aggregateClass) {
		Field field = findIdByAnnotation(aggregateClass);

		if (field == null) {
			field = findIdByNameConvention(aggregateClass);
		}

		field.setAccessible(true);
		return field;
	}

	private static Field findIdByAnnotation(Class<?> aggregateClass) {
		Optional<Field> field = findAnnotatedField(aggregateClass);

		if (field.isEmpty() && aggregateClass.getSuperclass() != null) {
			field = findAnnotatedField(aggregateClass.getSuperclass());
		}

		return field.orElse(null);
	}

	private static Optional<Field> findAnnotatedField(Class<?> clazz) {
		return Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> f.isAnnotationPresent(AggregateId.class))
				.findFirst();
	}

	private static Field findIdByNameConvention(Class<?> aggregateClass) {
		Field field;
		try {
			field = aggregateClass.getDeclaredField(NAME_OF_ID_FIELD);

		} catch (NoSuchFieldException e) {

			try {
				field = aggregateClass.getSuperclass().getDeclaredField(NAME_OF_ID_FIELD);
			} catch (NoSuchFieldException ex) {
				throw new IllegalAggregateIdException("Aggregate Must Have 'id' field or have @AggregateId annotation.", e);
			}

		} catch (Exception e) {
			throw new IllegalAggregateIdException("Aggregate Must Have 'id' field or have @AggregateId annotation.", e);
		}

		if (isNotSupport(field)) {
			throw new IllegalAggregateIdException("An aggregate id should be primitive long type.", null);
		}
		return field;
	}

	private static boolean isNotSupport(Field field) {
		return Arrays.stream(SUPPORTED_TYPES).noneMatch(t -> t.getTypeName().equals(field.getType().getTypeName()));
	}

	public static boolean notAssignedIdOf(Object aggregate) {
		Field field = getIdField(aggregate.getClass());
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
