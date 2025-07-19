/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import com.deyo.rbw.childclasses.configuration.Configuration;
import com.deyo.rbw.childclasses.configuration.ConfigurationSection;
import com.deyo.rbw.childclasses.configuration.InvalidConfigurationException;
import com.deyo.rbw.childclasses.configuration.file.FileConfiguration;
import com.deyo.rbw.childclasses.configuration.file.YamlConfigurationOptions;
import com.deyo.rbw.childclasses.configuration.file.YamlConstructor;
import com.deyo.rbw.childclasses.configuration.file.YamlRepresenter;

public class YamlConfiguration
extends FileConfiguration {
    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";
    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);
    public Object reload;

    @Override
    public String saveToString() {
        this.yamlOptions.setIndent(this.options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlOptions.setAllowUnicode(SYSTEM_UTF);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        String header = this.buildHeader();
        String dump = this.yaml.dump(this.getValues(false));
        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }
        return header + dump;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Map input;
        Validate.notNull(contents, "Contents cannot be null", new Object[0]);
        try {
            input = (Map)this.yaml.load(contents);
        }
        catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        }
        catch (ClassCastException ex) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }
        String header = this.parseHeader(contents);
        if (header.length() > 0) {
            this.options().header(header);
        }
        if (input != null) {
            this.convertMapsToSections(input, this);
        }
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                this.convertMapsToSections((Map)value, section.createSection(key));
                continue;
            }
            section.set(key, value);
        }
    }

    protected String parseHeader(String input) {
        String[] lines = input.split("\r?\n", -1);
        StringBuilder result = new StringBuilder();
        boolean readingHeader = true;
        boolean foundHeader = false;
        for (int i = 0; i < lines.length && readingHeader; ++i) {
            String line = lines[i];
            if (line.startsWith(COMMENT_PREFIX)) {
                if (i > 0) {
                    result.append("\n");
                }
                if (line.length() > COMMENT_PREFIX.length()) {
                    result.append(line.substring(COMMENT_PREFIX.length()));
                }
                foundHeader = true;
                continue;
            }
            if (foundHeader && line.length() == 0) {
                result.append("\n");
                continue;
            }
            if (!foundHeader) continue;
            readingHeader = false;
        }
        return result.toString();
    }

    @Override
    protected String buildHeader() {
        FileConfiguration filedefaults;
        String defaultsHeader;
        Configuration def;
        String header = this.options().header();
        if (this.options().copyHeader() && (def = this.getDefaults()) instanceof FileConfiguration && (defaultsHeader = (filedefaults = (FileConfiguration)def).buildHeader()) != null && defaultsHeader.length() > 0) {
            return defaultsHeader;
        }
        if (header == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;
        for (int i = lines.length - 1; i >= 0; --i) {
            builder.insert(0, "\n");
            if (!startedHeader && lines[i].length() == 0) continue;
            builder.insert(0, lines[i]);
            builder.insert(0, COMMENT_PREFIX);
            startedHeader = true;
        }
        return builder.toString();
    }

    @Override
    public YamlConfigurationOptions options() {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions)this.options;
    }

    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null", new Object[0]);
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        }
        catch (InvalidConfigurationException | IOException ex) {
            System.out.println("Cannot load " + file);
        }
        return config;
    }

    @Deprecated
    public static YamlConfiguration loadConfiguration(InputStream stream) {
        Validate.notNull(stream, "Stream cannot be null", new Object[0]);
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(stream);
        }
        catch (InvalidConfigurationException | IOException ex) {
            System.out.println("Cannot load configuration from stream");
        }
        return config;
    }

    public static YamlConfiguration loadConfiguration(Reader reader) {
        Validate.notNull(reader, "Stream cannot be null", new Object[0]);
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(reader);
        }
        catch (InvalidConfigurationException | IOException ex) {
            System.out.println("Cannot load configuration from stream");
        }
        return config;
    }
}

