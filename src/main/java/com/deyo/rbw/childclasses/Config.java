/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;

public class Config {
    public static YamlConfiguration config;

    public static void loadConfig() {
        String filename = "config.yml";
        File file = new File("RBW/" + filename);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Optionally: write some default config content here
                System.err.println("Created empty config at " + file.getAbsolutePath() + ". Please fill it in.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(file);
        System.out.println("Successfully loaded the config file into memory");
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(new File("RBW/config.yml"));
        Config.loadConfig();
    }

    public static String getValue(String key) {
        return config.getString(key);
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
}

