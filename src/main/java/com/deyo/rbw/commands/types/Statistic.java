/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.types;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.deyo.rbw.classes.player.Player;

public enum Statistic {
    ELO("elo", "points"),
    PEAKELO("peakelo", "maxelo", "elopeak"),
    WIN("wins", "win", "victories"),
    WS("ws", "winstreak"),
    HIGHESTWS("highestws", "highestwinstreak", "hws", "maxws", "maxwinstreak"),
    LOSSES("losses", "lost", "loses", "looses"),
    LS("ls", "losestreak"),
    HIGHESTLS("highestls", "highestlosestreak", "hls", "maxls", "maxlosestreak"),
    MVP("mvp", "mvps"),
    KILLS("kills", "kill", "kil"),
    DEATHS("deaths", "deaths", "death"),
    BEDS("beds", "bed"),
    STRIKES("strikes", "strike", "striked"),
    SCORED("scored", "scoredgames", "scoredgame"),
    GOLD("gold", "golds"),
    SCREENSHARED("screenshared", "ssed"),
    GAMES("games", "gamesplayed", "gm", "gp"),
    WLR("wlr", "wl"),
    KDR("kdr", "kd");

    private final List<String> aliases;
    private final String path;

    private Statistic(String pathname, String ... aliases) {
        this.path = pathname;
        this.aliases = List.of(aliases);
    }

    public String getPath() {
        return this.path;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public static Statistic getFromString(String sr) {
        for (Statistic value : Statistic.values()) {
            if (value.getPath().equalsIgnoreCase(sr)) {
                return value;
            }
            for (String alias : value.getAliases()) {
                if (!alias.equalsIgnoreCase(sr)) continue;
                return value;
            }
        }
        return null;
    }

    private static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getPercentage(String ID2) {
        if (LOSSES.getForPlayer(ID2) == 0.0 && WIN.getForPlayer(ID2) == 0.0) {
            return 0;
        }
        double tempgames = GAMES.getForPlayer(ID2);
        double tempwins = WIN.getForPlayer(ID2) == 0.0 ? 1.0 : WIN.getForPlayer(ID2);
        return (int)(tempwins / tempgames) * 100;
    }

    private double getWlr(String ID2) {
        if (LOSSES.getForPlayer(ID2) == 0.0 && WIN.getForPlayer(ID2) == 0.0) {
            return 0.0;
        }
        double templosses = LOSSES.getForPlayer(ID2) == 0.0 ? 1.0 : LOSSES.getForPlayer(ID2);
        double tempwins = WIN.getForPlayer(ID2) == 0.0 ? 1.0 : WIN.getForPlayer(ID2);
        double wlra = tempwins / templosses;
        return Statistic.round(wlra);
    }

    private double getKdr(String ID2) {
        if (DEATHS.getForPlayer(ID2) == 0.0 && KILLS.getForPlayer(ID2) == 0.0) {
            return 0.0;
        }
        double tempdeaths = DEATHS.getForPlayer(ID2) == 0.0 ? 1.0 : DEATHS.getForPlayer(ID2);
        double tempkills = KILLS.getForPlayer(ID2) == 0.0 ? 1.0 : KILLS.getForPlayer(ID2);
        double kdra = tempkills / tempdeaths;
        return Statistic.round(kdra);
    }

    public double getForPlayer(String ID2) {
        if (this == WLR) {
            return this.getWlr(ID2);
        }
        if (this == KDR) {
            return this.getKdr(ID2);
        }
        if (this == GAMES) {
            return WIN.getForPlayer(ID2) + LOSSES.getForPlayer(ID2);
        }
        return Player.getPlayers().get(ID2).getConfig().getDouble(this.getPath(), 0.0);
    }

    public void setForPlayer(String ID2, double value) {
        if (this == WLR || this == GAMES || this == KDR) {
            return;
        }
        if (value < 0.0) {
            value = 0.0;
        }
        Player.getPlayers().get(ID2).getConfig().set(this.getPath(), value);
        if (this == ELO && PEAKELO.getForPlayer(ID2) < value) {
            PEAKELO.setForPlayer(ID2, value);
        }
    }
}

