/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes.player;

import java.io.File;
import java.io.IOException;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.util.AtomicFileUtil;

public class PlayerStoring {
    private final String ID;
    private final YamlConfiguration db;
    private final File f;

    public PlayerStoring(String ID2) {
        File f = new File("RBW/players/", ID2 + ".yml");
        try {
            f.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.ID = ID2;
        this.f = f;
        this.db = YamlConfiguration.loadConfiguration(f);
    }

    public PlayerStoring(String ID2, File file) {
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.ID = ID2;
        this.f = file;
        this.db = YamlConfiguration.loadConfiguration(file);
    }

    public void save() throws IOException {
        byte[] yamlBytes = db.saveToString().getBytes("UTF-8");
        AtomicFileUtil.atomicWrite(f, yamlBytes);
    }

    public YamlConfiguration getConfig() {
        return this.db;
    }

    public File getF() {
        return this.f;
    }

    public String getID() {
        return this.ID;
    }
}

