package com.labca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads config.properties; any key can be overridden with -Dkey=value. */
public final class Config {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties not found on classpath");
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config.properties", e);
        }
    }

    private Config() { }

    public static String get(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        if (value == null) throw new IllegalArgumentException("Missing config key: " + key);
        return value.trim();
    }

    public static String get(String key, String def) {
        String value = System.getProperty(key, PROPS.getProperty(key, def));
        return value == null || value.isBlank() ? def : value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
