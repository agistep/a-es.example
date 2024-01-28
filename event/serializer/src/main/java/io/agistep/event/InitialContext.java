package io.agistep.event;

import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;

import java.lang.reflect.Constructor;
import java.util.Arrays;

class InitialContext {

    public Serializer serializerLookup(String name) {
        Class<?> cls = null;
        Serializer ser = new NoOpSerializer();
        try {
            try {
                cls = Class.forName(name);
            } catch (ClassNotFoundException e) {
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                if (cl != null) {
                    cls = cl.loadClass(name);
                }
            }
            if (cls != null) {
                @SuppressWarnings("deprecation")
                Object tmp = cls.newInstance();
                ser = (Serializer) tmp;
            }
        } catch (Exception e) {
            // any number of exceptions can get thrown here
        }
        return ser;
    }

    public Deserializer deserializerLookup(String className, Class<?> clazz) {
        Class<?> cls = null;
        Deserializer deserializer = new NoOpDeserializer();
        try {
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                ClassLoader cl = ClassLoader.getSystemClassLoader();
                if (cl != null) {
                    cls = cl.loadClass(className);
                }
            }

            Constructor<?>[] constructors = cls.getDeclaredConstructors();

            Constructor<?> constructor = Arrays.stream(constructors)
                    .filter(c -> c.getParameters().length == 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalAccessException("Custom Deserializer class must have only one targetClass Parameter for deserializing"));

            if (cls != null) {
                Object tmp = constructor.newInstance(clazz);
                deserializer = (Deserializer) tmp;
            }
        } catch (Exception e) {
            // any number of exceptions can get thrown here
        }
        return deserializer;
    }
}
