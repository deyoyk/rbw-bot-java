/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javax.imageio.ImageIO;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.commands.types.Statistic;

public class Themes {
    private final String File;
    private final long Cost;
    private String Name;
    private YamlConfiguration Config;
    private int defaultfontsize;
    private int namefontsize;
    private final HashMap<Statistic, ThemeFormat> themeFormats = new HashMap();
    public static ArrayList<Themes> themes = new ArrayList();

    public int getNameFontSize() {
        return this.namefontsize;
    }

    public int getDefaultFontSize() {
        return this.defaultfontsize;
    }

    public BufferedImage getImage() {
        try {
            return ImageIO.read(this.getThemeImage());
        }
        catch (Exception e) {
            return null;
        }
    }

    public YamlConfiguration getConfig() {
        return this.Config;
    }

    public Themes(String filename, YamlConfiguration config) {
        this.Config = config;
        this.Name = filename.split("\\.")[0];
        this.File = filename;
        this.Cost = config.getLong("cost");
        if (config.isSet("formats") && config.isSet("formats.default")) {
            String defColor = config.getString("formats.default.color", "0,0,0");
            Integer[] ints = this.getArray(defColor);
            Color defaultColor = new Color(ints[0], ints[1], ints[2]);
            this.namefontsize = config.getInt("name-fontsize", 35);
            this.defaultfontsize = config.getInt("formats.default.fontsize", 30);
            ThemeFormat defaultFormat = new ThemeFormat(defaultColor, config.getString("formats.default.format", "%v"), this.defaultfontsize);
            for (Statistic stat : Statistic.values()) {
                if (config.isSet("formats." + stat.getPath())) {
                    String formatColor = config.getString("formats." + stat.getPath() + ".color", defColor);
                    Integer[] intsFormat = this.getArray(formatColor);
                    Color sColor = new Color(intsFormat[0], intsFormat[1], intsFormat[2]);
                    this.themeFormats.put(stat, new ThemeFormat(sColor, config.getString("formats." + stat.getPath() + ".format", "%v"), config.getInt("formats." + stat.getPath() + ".fontsize", this.defaultfontsize)));
                    continue;
                }
                this.themeFormats.put(stat, defaultFormat);
            }
        }
    }

    public void reloadConfig() {
        themes.clear();
        Themes.load();
    }

    public HashMap<Statistic, ThemeFormat> getThemeFormats() {
        return this.themeFormats;
    }

    public long getCost() {
        return this.Cost;
    }

    public String getFile() {
        return this.File;
    }

    public File getThemeImage() {
        File result = null;
        String name = this.getName().toLowerCase() + ".";
        for (File f : new File("RBW/themes").listFiles()) {
            if (f.getName().contains(".yml") || !f.getName().toLowerCase().startsWith(name)) continue;
            result = f;
        }
        return result;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getName() {
        return this.Name;
    }

    public static void load() {
        for (File f : new File("RBW/themes").listFiles()) {
            if (!f.getName().contains(".yml")) continue;
            YamlConfiguration data = YamlConfiguration.loadConfiguration(new File("RBW/themes/" + f.getName()));
            themes.add(new Themes(f.getName(), data));
        }
    }

    public static ArrayList<Themes> list() {
        return themes;
    }

    public static ArrayList<Themes> list(String collection) {
        ArrayList<Themes> result = new ArrayList<Themes>();
        for (Themes s2 : Themes.list()) {
            if (!s2.getName().toLowerCase().startsWith(collection.toLowerCase())) continue;
            result.add(s2);
        }
        return result;
    }

    public static void addTheme(String name, long cost) {
        YamlConfiguration data = null;
        try {
            File f = new File("RBW/themes/" + name + ".yml");
            f.createNewFile();
            data = YamlConfiguration.loadConfiguration(f);
            data.set("cost", cost);
            data.save(f);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        assert (data != null);
        themes.add(new Themes(name + ".yml", data));
    }

    public static void removeTheme(String file) {
        themes.removeIf(theme -> theme.getFile().equals(file));
        try {
            Files.deleteIfExists(Paths.get("RBW/themes/" + file, new String[0]));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Themes getTheme(String name) {
        for (Themes theme : Themes.list()) {
            if (!theme.getFile().toLowerCase().startsWith(name.toLowerCase())) continue;
            return theme;
        }
        if (Objects.equals(name, "default")) {
            return null;
        }
        return Themes.getTheme("default");
    }

    public static boolean isCollection(String collection) {
        for (String collecttocheck : Themes.getCollections()) {
            if (!collecttocheck.toLowerCase().contains(collection.toLowerCase())) continue;
            return true;
        }
        return false;
    }

    public static ArrayList<String> getCollections() {
        ArrayList<String> result = new ArrayList<String>();
        for (Themes s2 : Themes.list()) {
            String collection = s2.getName().contains("_") ? s2.getName().split("_")[0] : s2.getName();
            if (result.contains(collection)) continue;
            result.add(collection);
        }
        return result;
    }

    Integer[] getArray(String path) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (String s2 : path.split(",")) {
            ints.add(Integer.parseInt(s2));
        }
        return ints.toArray(new Integer[0]);
    }

    public static class ThemeFormat {
        public Color color;
        public String format;
        public int fontsize;

        public ThemeFormat(Color color, String format, int fontsize) {
            this.color = color;
            this.format = format;
            this.fontsize = fontsize;
        }
    }
}

