package net.advancius.configuration;

public class EnvironmentConfiguration implements Configuration {

    @Override
    public String getProperty(String path) {
        return System.getenv(path);
    }

    @Override
    public String getProperty(String path, String defaultValue) {
        return System.getenv().getOrDefault(path, defaultValue);
    }
}
