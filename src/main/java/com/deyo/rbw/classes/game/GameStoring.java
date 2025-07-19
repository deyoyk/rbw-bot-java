/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes.game;

import java.io.File;
import java.io.IOException;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;

public class GameStoring {
    private final int gamenumber;
    private final YamlConfiguration db;
    private final File f;

    public GameStoring(int gamenumber) {
        File f = new File("RBW/games", gamenumber + ".yml");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.gamenumber = gamenumber;
        this.f = f;
        this.db = YamlConfiguration.loadConfiguration(f);
    }

    public GameStoring(int gamenumber, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.gamenumber = gamenumber;
        this.f = file;
        this.db = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig() {
        return this.db;
    }

    public int getGamenumber() {
        return this.gamenumber;
    }

    public File getF() {
        return this.f;
    }
}

