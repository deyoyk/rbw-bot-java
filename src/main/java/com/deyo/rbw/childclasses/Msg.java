/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;

public class Msg {
    public static YamlConfiguration db;

    public static void loadMsg() throws FileNotFoundException {
        File file = new File("RBW/messages.yml");
        if (!file.exists()) {
            String filename = "messages.yml";
            ClassLoader classLoader = Main.class.getClassLoader();
            try (InputStream inputStream2 = classLoader.getResourceAsStream(filename);){
                String result = IOUtils.toString(inputStream2, StandardCharsets.UTF_8);
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(result);
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        db = YamlConfiguration.loadConfiguration(new File("RBW/messages.yml"));
        for (Messages value : Messages.values()) {
            if (db.isSet(value.getPath())) continue;
            db.set(value.getPath(), "Not set in messages.yml (" + value.getPath() + ")");
        }
        try {
            db.save(file);
        }
        catch (Exception exception) {
            // empty catch block
        }
        System.out.println("Successfully loaded the messages file into memory");
    }

    public static void reloadMsg() {
        try {
            Msg.loadMsg();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getMsg(String path) {
        return db.getString(path);
    }
}

