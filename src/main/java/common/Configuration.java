package common;

import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Getter
@Log4j2
@Singleton
public class Configuration {

    private static Configuration instance = null;
    private final Properties p;

    public Configuration() {
        p = new Properties();
        try {
            p.loadFromXML(Files.newInputStream(Paths.get("src/main/resources/mysql-properties.xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return p.getProperty(key);
    }
}
