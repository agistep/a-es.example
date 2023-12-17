package io.agistep.event;

import io.agistep.event.serialization.NoOpDeserializer;
import io.agistep.event.serialization.NoOpSerializer;

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

    public Deserializer deserializerLookup(String className) {
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
            if (cls != null) {
                @SuppressWarnings("deprecation")
                Object tmp = cls.newInstance();
                deserializer = (Deserializer) tmp;
            }
        } catch (Exception e) {
            // any number of exceptions can get thrown here
        }
        return deserializer;
    }
}
