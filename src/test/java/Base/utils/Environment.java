package Base.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Environment {

    private static final Properties properties = new Properties();

    public static String get(String key) {

        File file = new File("src/test/resources/routs/routs.properties");
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            System.err.println("ОШИБКА: свойство отсутствует!");
        }
        return properties.getProperty(key);
    }
}
