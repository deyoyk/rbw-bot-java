/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.util.ArrayList;
import java.util.Collection;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class EloBounties {
    public static ArrayList<EloBounty> perWinstreak = new ArrayList();
    public static ArrayList<EloBounty> perElo = new ArrayList();

    private static int getMin(String path) {
        int min2 = 0;
        if (Config.getConfig().isSet(path + ".after")) {
            String reached = Config.getConfig().getString(path + ".after");
            min2 = EloBounties.getFormatted(reached);
        } else if (Config.getConfig().isSet(path + ".between")) {
            String between = Config.getConfig().getString(path + ".between");
            String minFormat = between.split("-")[0];
            min2 = EloBounties.getFormatted(minFormat);
        }
        return min2;
    }

    private static int getMax(String path) {
        int max = Integer.MAX_VALUE;
        if (Config.getConfig().isSet(path + ".between")) {
            String between = Config.getConfig().getString(path + ".between");
            String maxFormat = between.split("-")[1];
            max = EloBounties.getFormatted(maxFormat);
        }
        return max;
    }

    public static void load() {
        int giveElo;
        int max;
        int min2;
        String path;
        int i;
        for (i = 1; i < 20; ++i) {
            path = "elo-bounty.per-winstreak." + i;
            if (!Config.getConfig().isSet(path)) break;
            min2 = EloBounties.getMin(path);
            max = EloBounties.getMax(path);
            giveElo = EloBounties.getFormatted(Config.getValue(path + ".give"));
            perWinstreak.add(new EloBounty(min2, max, giveElo));
        }
        for (i = 1; i < 20; ++i) {
            path = "elo-bounty.per-elo." + i;
            if (!Config.getConfig().isSet(path)) break;
            min2 = EloBounties.getMin(path);
            max = EloBounties.getMax(path);
            giveElo = EloBounties.getFormatted(Config.getValue(path + ".give"));
            perWinstreak.add(new EloBounty(min2, max, giveElo));
        }
    }

    private static int getFormatted(String sr) {
        return Integer.parseInt(sr.replaceAll("ELO", "").replaceAll("WS", ""));
    }

    public static void checkEloBounties(int gamenumber, Collection<String> winningTeam, Collection<String> losingTeam, Guild g2) {
        int worth = 0;
        Object targets = "";
        Object mentions = "";
        for (String id : losingTeam) {
            String s2;
            mentions = (String)mentions + "<@" + id + ">";
            int togiveElo = 0;
            boolean gained = false;
            Object str = "";
            for (EloBounty eloBounty : perWinstreak) {
                if (!eloBounty.canApply((int)Statistic.WS.getForPlayer(id))) continue;
                s2 = eloBounty.getMinReq() + (String)(eloBounty.getMaxReq() == Integer.MAX_VALUE ? "+" : "-" + eloBounty.getMaxReq());
                gained = true;
                str = (String)str + "- <@" + id + "> (" + s2 + ") ";
                worth += eloBounty.getGiveELO();
                togiveElo += eloBounty.getGiveELO();
            }
            for (EloBounty eloBounty : perElo) {
                if (!eloBounty.canApply((int)Statistic.ELO.getForPlayer(id))) continue;
                s2 = eloBounty.getMinReq() + (String)(eloBounty.getMaxReq() == Integer.MAX_VALUE ? "+" : "-" + eloBounty.getMaxReq());
                str = !gained ? (String)str + "- <@" + id + "> (" + s2 + ")" : (String)str + " (" + s2 + ")";
                worth += eloBounty.getGiveELO();
                togiveElo += eloBounty.getGiveELO();
            }
            if (togiveElo == 0) continue;
            if (togiveElo > Config.getConfig().getInt("bounties-cap")) {
                togiveElo = Config.getConfig().getInt("bounties-cap");
            }
            str = (String)str + " (Gain: `+" + togiveElo + "` ELO)\n";
            Statistic.ELO.setForPlayer(id, Statistic.ELO.getForPlayer(id) + (double)togiveElo);
            targets = (String)targets + (String)str;
        }
        if (worth != 0) {
            String title = "Game #" + gamenumber + " Bounties";
            String description = "**Bounty's Reward:** `+" + worth + "` ELO\n";
            description = description + "**Bounty's Winners:**\n";
            for (String id : winningTeam) {
                description = description + "- <@" + id + ">\n";
            }
            description = description + "**Bounty's Targets:**\n";
            description = description + (String)targets;
            try {
                BetterEmbed bounties = new BetterEmbed("default", title, "", description, "");
                ((MessageCreateAction)RBW.bountiesChannel.sendMessage((CharSequence)mentions).setEmbeds(bounties.build())).queue();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static class EloBounty {
        private final int minReq;
        private final int maxReq;
        private final int giveELO;

        public EloBounty(int minReq, int maxReq, int giveElo) {
            this.minReq = minReq;
            this.maxReq = maxReq;
            this.giveELO = giveElo;
        }

        public boolean canApply(int min2) {
            return min2 >= this.minReq && min2 <= this.maxReq;
        }

        public int getGiveELO() {
            return this.giveELO;
        }

        public int getMaxReq() {
            return this.maxReq;
        }

        public int getMinReq() {
            return this.minReq;
        }
    }
}

