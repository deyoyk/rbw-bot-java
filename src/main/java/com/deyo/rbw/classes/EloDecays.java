/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class EloDecays {
    static YamlConfiguration db;
    static File file;

    public static void load() throws IOException {
        file = new File("RBW/decays.yml");
        if (!file.exists()) {
            file.createNewFile();
        }
        db = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            db.save(file);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void check(String ID2) {
        Rank r = Rank.getPlayerRank(ID2);
        if (r != null && r.getDecay() != -1) {
            List<Game> recentGames = Game.getPlayerGames(ID2, 2);
            recentGames.sort((game, game1) -> Math.toIntExact(game1.getStartedAt() - game.getStartedAt()));
            if (recentGames.isEmpty()) {
                return;
            }
            long gameDiff = System.currentTimeMillis() - recentGames.get(0).getStartedAt();
            long daysGameDiff = TimeUnit.MILLISECONDS.toDays(gameDiff);
            if (daysGameDiff < (long)Config.getConfig().getInt("elo-decay.every-days")) {
                return;
            }
            int decay = r.getDecay();
            long lastDecayed = db.getLong(ID2 + ".lastdecayed", 0L);
            long millis = System.currentTimeMillis() - lastDecayed;
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            if (lastDecayed == 0L || days >= (long)Config.getConfig().getInt("elo-decay.every-days")) {
                db.set(ID2 + ".lastdecayed", System.currentTimeMillis());
                int oldelo = (int)Statistic.ELO.getForPlayer(ID2);
                Statistic.ELO.setForPlayer(ID2, oldelo - decay);
                BetterEmbed decayed = new BetterEmbed("default", "Decayed", "", "Elo decay got triggered.", "");
                decayed.addField("User", "<@" + ID2 + "> (`" + ID2 + "`)", false);
                decayed.addField("Last Play", "`" + daysGameDiff + "` **days ago**", false);
                decayed.addField("Amount", "`-" + decay + "` ELO", false);
                decayed.addField("Change", "`" + oldelo + " -> " + (oldelo - decay) + "`", false);
                ((MessageCreateAction)RBW.eloDecayAnnouncementsChannel.sendMessage("<@" + ID2 + ">").setEmbeds(decayed.build())).queue();
            }
        }
    }
}

