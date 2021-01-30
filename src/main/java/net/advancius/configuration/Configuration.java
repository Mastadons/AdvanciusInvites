package net.advancius.configuration;

public interface Configuration {

    String getProperty(String path);
    String getProperty(String path, String defaultValue);
}
