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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;

public class Perms {
    public static YamlConfiguration db = null;
    public static HashMap<String, List<String>> groups = new HashMap();
    public static String filename = "permissions.yml";
    public static File f = new File("RBW/" + filename);

    public static void loadGroups() {
        for (Map.Entry<String, Object> entry : db.getValues(true).entrySet()) {
            String group;
            String e = entry.getKey();
            if (!e.startsWith("groups.") || (group = e.split("\\.")[1]).contains(".")) continue;
            groups.put(group, db.getStringList(e));
        }
    }

    public static void save() {
        if (db != null) {
            try {
                db.save(f);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadPerms() {
        ClassLoader classLoader = Main.class.getClassLoader();
        if (!f.exists()) {
            try (InputStream inputStream2 = classLoader.getResourceAsStream(filename);){
                String result = IOUtils.toString(inputStream2, StandardCharsets.UTF_8);
                f.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                bw.write(result);
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        db = YamlConfiguration.loadConfiguration(f);
        Perms.loadGroups();
        System.out.println("Successfully loaded the permissions file into memory");
    }

    public static void reloadPerms() {
        db = YamlConfiguration.loadConfiguration(f);
        groups.clear();
        Perms.loadGroups();
    }

    public static YamlConfiguration getDb() {
        return db;
    }
}

