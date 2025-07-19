/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.commands.types.Statistic;

public class Rank {
    static ArrayList<Rank> ranks = new ArrayList();
    private String id;
    private int StartingElo;
    private int EndingElo;
    private int WinElo;
    private int LoseElo;
    private int MvpElo;
    private int BedElo;
    private int decay = -1;
    public final File file;
    public Image rankImage = null;
    public final YamlConfiguration db;

    public Rank(File f) {
        File image;
        this.file = f;
        this.db = YamlConfiguration.loadConfiguration(f);
        this.load(f.getName().replaceAll(".yml", ""), this.db.getInt("startingElo"), this.db.getInt("endingElo"), this.db.getInt("winElo"), this.db.getInt("loseElo"), this.db.getInt("mvpElo"), this.db.getInt("bedElo"));
        if (this.db.getInt("decayElo", -1) != -1) {
            this.setDecay(this.db.getInt("decayElo"));
        }
        if ((image = new File("RBW/rankimages/" + this.id + ".png")).exists()) {
            try {
                this.rankImage = ImageIO.read(image);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void load(String ID2, int starting, int ending, int win, int lose, int mvp, int bed) {
        this.id = ID2;
        this.StartingElo = starting;
        this.EndingElo = ending;
        this.WinElo = win;
        this.LoseElo = lose;
        this.MvpElo = mvp;
        this.BedElo = bed;
    }

    public void refresh() {
        this.db.set("decayElo", this.decay);
        this.db.set("mvpElo", this.MvpElo);
        this.db.set("endingElo", this.EndingElo);
        this.db.set("loseElo", this.LoseElo);
        this.db.set("winElo", this.WinElo);
        this.db.set("startingElo", this.StartingElo);
        this.db.set("bedElo", this.BedElo);
        this.save();
    }

    public void setDecay(int decay) {
        this.decay = decay;
        this.refresh();
    }

    public void setMvpElo(int mvpElo) {
        this.MvpElo = mvpElo;
        this.refresh();
    }

    public void setEndingElo(int endingElo) {
        this.EndingElo = endingElo;
        this.refresh();
    }

    public void setLoseElo(int loseElo) {
        this.LoseElo = loseElo;
        this.refresh();
    }

    public void setWinElo(int winElo) {
        this.WinElo = winElo;
        this.refresh();
    }

    public void setStartingElo(int startingElo) {
        this.StartingElo = startingElo;
        this.refresh();
    }

    public void setBedElo(int bedElo) {
        this.BedElo = bedElo;
        this.refresh();
    }

    public void save() {
        try {
            this.db.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getEndingElo() {
        return this.EndingElo;
    }

    public int getLoseElo() {
        return this.LoseElo;
    }

    public int getMvpElo() {
        return this.MvpElo;
    }

    public int getStartingElo() {
        return this.StartingElo;
    }

    public int getWinElo() {
        return this.WinElo;
    }

    public int getBedElo() {
        return this.BedElo;
    }

    public String getId() {
        return this.id;
    }

    public static void loadRanks() {
        File[] list = new File("RBW/ranks").listFiles();
        if (list == null) {
            System.out.println("No files in ranks.");
            return;
        }
        if (list.length > 0) {
            for (File f : list) {
                ranks.add(new Rank(f));
            }
            System.out.println("Successfully loaded all ranks into memory");
        }
    }

    public int getDecay() {
        return this.decay;
    }

    public static void saveData() {
        for (Rank rank : ranks) {
            rank.save();
        }
    }

    public static Rank getRankFromID(String id) {
        Rank result = null;
        for (Rank rank : ranks) {
            if (!rank.getId().equals(id)) continue;
            result = rank;
            break;
        }
        return result;
    }

    public static ArrayList<Rank> getRanks() {
        return ranks;
    }

    public static Rank addRank(String ID2, String startingElo, String endingElo, String winElo, String loseElo, String mvpElo, String bedElo) {
        File f = new File("RBW/ranks/" + ID2 + ".yml");
        int sElo = Integer.parseInt(startingElo.replaceAll("-", ""));
        int eElo = Integer.parseInt(endingElo.replaceAll("-", ""));
        int wElo = Integer.parseInt(winElo.replaceAll("-", ""));
        int lElo = Integer.parseInt(loseElo.replaceAll("-", ""));
        int mElo = Integer.parseInt(mvpElo.replaceAll("-", ""));
        int bElo = Integer.parseInt(bedElo.replaceAll("-", ""));
        try {
            f.createNewFile();
            YamlConfiguration db = YamlConfiguration.loadConfiguration(f);
            db.set("startingElo", sElo);
            db.set("endingElo", eElo);
            db.set("winElo", wElo);
            db.set("loseElo", lElo);
            db.set("mvpElo", mElo);
            db.set("bedElo", bElo);
            db.save(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Rank r = new Rank(f);
        ranks.add(r);
        return r;
    }

    public static void delRank(String ID2) {
        try {
            Files.deleteIfExists(Paths.get("RBW/ranks/" + ID2 + ".yml", new String[0]));
            ranks.removeIf(rank -> rank.getId().equals(ID2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Rank getPlayerRank(String ID2) {
        int elo = (int)Statistic.ELO.getForPlayer(ID2);
        for (Rank rank : ranks) {
            int StartingElo = rank.getStartingElo();
            int EndingElo = rank.getEndingElo();
            if (elo < StartingElo || elo > EndingElo) continue;
            return rank;
        }
        return null;
    }

    public Rank parseRank(String ID2) {
        Rank result = null;
        for (Rank rank : ranks) {
            if (!rank.getId().equals(ID2)) continue;
            result = rank;
            break;
        }
        return result;
    }
}

