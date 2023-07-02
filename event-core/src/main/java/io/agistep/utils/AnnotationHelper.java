package io.agistep.utils;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public final class AnnotationHelper {

	public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		return !getMethodsListWithAnnotation(clazz, annotationClass).isEmpty();
	}

	public static List<Method> getMethodsListWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		return MethodUtils.getMethodsListWithAnnotation(clazz, annotationClass, true, true);
	}

	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationClass) {
		return MethodUtils.getAnnotation(method, annotationClass, true, true);
	}

	public static boolean objectHasAnnotation(Object object, Class<? extends Annotation> annotationClass) {
		return hasAnnotation(object.getClass(), annotationClass);
	}

}
