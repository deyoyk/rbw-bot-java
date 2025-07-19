/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes.guild;

import java.io.File;
import java.io.IOException;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;

public class GuildStoring {
    private final String guildName;
    private final YamlConfiguration db;
    private final File f;

    public GuildStoring(String guildName) {
        File f = new File("RBW/guilds", guildName + ".yml");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.guildName = guildName;
        this.f = f;
        this.db = YamlConfiguration.loadConfiguration(f);
    }

    public GuildStoring(String guildName, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.guildName = guildName;
        this.f = file;
        this.db = YamlConfiguration.loadConfiguration(file);
    }

    public void remove() {
        this.f.delete();
    }

    public YamlConfiguration getConfig() {
        return this.db;
    }

    public String getGuildName() {
        return this.guildName;
    }

    public File getF() {
        return this.f;
    }
}

