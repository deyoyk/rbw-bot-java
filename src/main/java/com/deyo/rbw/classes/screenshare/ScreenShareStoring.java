/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes.screenshare;

import java.io.File;
import java.io.IOException;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;

public class ScreenShareStoring {
    String screenShared;
    YamlConfiguration db;
    File f;

    public ScreenShareStoring(String screenShared) {
        File f = new File("RBW/screenshares", screenShared + ".yml");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.screenShared = screenShared;
        this.f = f;
        this.db = YamlConfiguration.loadConfiguration(f);
    }

    public ScreenShareStoring(String screenShared, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.screenShared = screenShared;
        this.f = file;
        this.db = YamlConfiguration.loadConfiguration(file);
    }

    public void delete() {
        this.db = null;
        this.screenShared = null;
        this.f.delete();
        this.f = null;
    }

    public YamlConfiguration getConfig() {
        return this.db;
    }

    public String getScreenShared() {
        return this.screenShared;
    }

    public File getF() {
        return this.f;
    }
}

