package com.iecas.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DLProperty {
    private static Properties properties;
    public static String getProperty(String key) throws IOException {
        properties = new Properties() ;
        properties.load(new FileInputStream("setting/properties"));
        return properties.getProperty(key);
    }
}
