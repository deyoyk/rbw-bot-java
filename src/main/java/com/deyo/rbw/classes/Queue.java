/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.util.AtomicFileUtil;

public class Queue {
    static ArrayList<Queue> queues = new ArrayList<>();
    private final String id;
    private final int playerinteam;
    private final boolean casual;
    private final String picking;
    private QueueMaps mapUsed;
    public final File f;
    public final YamlConfiguration data;

    public Queue(File file) {
        this.f = file;
        this.data = YamlConfiguration.loadConfiguration(file);
        this.id = file.getName().replaceAll(".yml", "");
        this.playerinteam = this.data.getInt("players-in-team");
        this.casual = this.data.getBoolean("casual");
        this.picking = this.data.getString("picking");
        this.mapUsed = QueueMaps.Quads;
        if (this.data.isSet("map")) {
            try {
                this.mapUsed = QueueMaps.valueOf(this.data.getString("map"));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        queues.add(this);
    }

    public QueueMaps getMapUsed() {
        return this.mapUsed;
    }

    public static int getPicks(int n) {
        if (n == 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 3;
        }
        if (n == 4) {
            return 5;
        }
        if (n == 5) {
            return 7;
        }
        return n + 4;
    }

    public String getPicking() {
        return this.picking;
    }

    public boolean isCasual() {
        return this.casual;
    }

    public int getPlayersInTeam() {
        return this.playerinteam;
    }

    public String getId() {
        return this.id;
    }

    public static void loadQueues() {
        if (new File("RBW/queues").listFiles().length > 0) {
            for (File f : new File("RBW/queues").listFiles()) {
                new Queue(f);
            }
            System.out.println("Successfully loaded all queues into memory");
        }
    }

    public static void saveData() {
        try {
            for (Queue queue : queues) {
                byte[] yamlBytes = queue.data.saveToString().getBytes("UTF-8");
                AtomicFileUtil.atomicWrite(queue.f, yamlBytes);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Queue> getQueues() {
        return queues;
    }

    public static Queue getQueueFromID(String ID2) {
        for (Queue queue : queues) {
            if (!queue.getId().equals(ID2)) continue;
            return queue;
        }
        return null;
    }

    public static void addQueue(String ID2, int playersInTeam, String pickingMode, boolean isCasual, QueueMaps mapUsed) {
        File f = new File("RBW/queues/" + ID2 + ".yml");
        try {
            f.createNewFile();
            YamlConfiguration data = YamlConfiguration.loadConfiguration(f);
            data.set("players-in-team", playersInTeam);
            data.set("picking", pickingMode);
            data.set("casual", isCasual);
            data.set("map", mapUsed.name());
            data.save(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        queues.add(new Queue(f));
    }

    public static void delQueue(String ID2) {
        try {
            Files.deleteIfExists(Paths.get("RBW/queues/" + ID2 + ".yml", new String[0]));
            queues.removeIf(q -> q.getId().equals(ID2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum QueueMaps {
        Doubles,
        Quads;

    }
}

