package org.avangard.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream in = new FileInputStream("config.yml")) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфигурационный файл.", e);
        }
    }

    public static String getString(String path) {
        String value = properties.getProperty(path);
        if (value == null)
            throw new IllegalArgumentException("Ключ не найден: " + path);
        return value;
    }

    public static int getInt(String path) {
        String value = getString(path);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значение не является числом: " + path);
        }
    }
}
