package io.agistep.utils;

import io.agistep.config.GamjaConfig;
import io.agistep.event.exception.GamjaComponentCreationException;

public class BasePackageLoader {

    private static final String BASE_PACKAGE_PROPERTY = "basePackage";


    public static String load() {
        var basePackage = GamjaConfig.getProperties().getBasePackage();

        if (basePackage == null) {
            basePackage = System.getProperty(BASE_PACKAGE_PROPERTY);
            if (basePackage == null) {
                throw new GamjaComponentCreationException("BasePackage should be set as a system property.");
            }
        }

        return basePackage;
    }

}
