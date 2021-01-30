package net.advancius.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FileConfiguration implements Configuration {

    private Properties properties = new Properties();

    public FileConfiguration(FileReader reader) throws IOException {
        properties.load(reader);
    }

    @Override
    public String getProperty(String path) {
        return properties.getProperty(path);
    }

    @Override
    public String getProperty(String path, String defaultValue) {
        return properties.getProperty(path, defaultValue);
    }
}
