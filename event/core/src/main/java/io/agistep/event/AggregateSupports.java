package io.agistep.event;

import io.agistep.identity.Identity;

import java.lang.reflect.Field;

public final class AggregateSupports {

	//TODO 암묵적인 규칙 컨팬션을 더 여러개 고려할수 있겠다.
	//TODO @ID
	public static long getId(Object aggregate) {
		try {
			Field field = aggregate.getClass().getDeclaredField("id");
			field.setAccessible(true);
			if(null == field.get(aggregate)) {
				return -1;
			}
			if (field.get(aggregate) instanceof Identity) {
				Identity<Long> aa = (Identity<Long>) (field.get(aggregate));
				if(aa == null) {
					return -1;
				}
				return aa.getValue();
			}
			throw new RuntimeException();
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
