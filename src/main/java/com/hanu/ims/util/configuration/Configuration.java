package com.hanu.ims.util.configuration;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Configuration class
 *
 * @see {@link org.apache.commons.configuration2}
 */
public class Configuration {

    private static Configuration instance;

    private static final String CONFIGURATION_PATH = "configurations";
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^(.+)\\.([A-Za-z0-9]+)$");
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    private List<FileBasedConfiguration> configurations;

    private Configuration() {
    }

    public void loadConfigurations() throws ConfigurationException {
        logger.info(System.getProperty("user.dir"));
        configurations = new ArrayList<>();
        File configurationFolder = null;

        try {
            configurationFolder = new File(CONFIGURATION_PATH);
            @SuppressWarnings("unused") Object o = configurationFolder.listFiles()[0];
        } catch (NullPointerException e) {
            configurationFolder = new File("./src/main/resources/".concat(CONFIGURATION_PATH));
        }

        Configurations configurations = new Configurations();
        for (File configurationFile : configurationFolder.listFiles()) {
            String fileName = configurationFile.getName();
            Matcher extMatcher = EXTENSION_PATTERN.matcher(fileName);
            extMatcher.find();
            switch (extMatcher.group(2)) {
                case "xml":
                    this.configurations.add(configurations.xml(configurationFile));
                    break;
                case "properties":
                    this.configurations.add(configurations.properties(configurationFile));
                    break;
                case "ini":
                    this.configurations.add(configurations.ini(configurationFile));
                    break;
                default:
                    break;
            }
            logger.info("Added configuration file: " + fileName);
        }
    }

    public String getString(String property) {
        return configurations.stream().filter(c -> c.containsKey(property))
                .map(c -> c.getString(property))
                .collect(Collectors.joining(""));
    }

    public static String get(String property) {
        return instance.getString(property);
    }

    @Override
    public String toString() {
        return configurations.toString();
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
