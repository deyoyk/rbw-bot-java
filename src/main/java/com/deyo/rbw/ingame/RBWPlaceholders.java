/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.ingame;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.guild.Guild;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;

public class RBWPlaceholders
extends PlaceholderExpansion {
    @NotNull
    public String getIdentifier() {
        return "rbw";
    }

    @NotNull
    public String getAuthor() {
        return "deyo";
    }

    @NotNull
    public String getVersion() {
        return "1.0";
    }

    @Nullable
    public String onPlaceholderRequest(org.bukkit.entity.Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        String ID2 = Player.getIdFromIGN(player.getName());
        if (params.equalsIgnoreCase("rank")) {
            String groupName = PlaceholderAPI.setPlaceholders((org.bukkit.entity.Player)player, (String)"%luckperms_primary_group_name%");
            return groupName.substring(0, 1).toUpperCase() + groupName.substring(1).toLowerCase();
        }
        if (params.startsWith("guild_")) {
            Statistic statistic;
            String[] s2 = params.split("_");
            if (ID2 == null) {
                return "";
            }
            Guild guild = Guild.getGuildByMember(ID2);
            if (s2[1].equalsIgnoreCase("name")) {
                if (guild != null) {
                    return guild.getGuildName();
                }
                return "Unknown";
            }
            if (s2[1].equalsIgnoreCase("prefix")) {
                if (guild != null) {
                    return "[" + guild.getGuildName() + "]";
                }
                return "Unknown";
            }
            String stat = s2[1].toUpperCase();
            try {
                statistic = Statistic.valueOf(stat);
            }
            catch (Exception e) {
                return "";
            }
            if (guild != null) {
                return String.valueOf(guild.getStatistic(statistic));
            }
            return "0";
        }
        if (params.startsWith("leaderboard_")) {
            int place;
            Statistic statistic;
            String[] s3 = params.split("_");
            String stat = s3[1].toUpperCase();
            String type = s3[3];
            try {
                statistic = Statistic.valueOf(stat);
            }
            catch (Exception ex) {
                return "0";
            }
            try {
                place = Integer.parseInt(s3[2]);
            }
            catch (Exception ex) {
                return "0";
            }
            HashMap<String, Double> unsortedMap = new HashMap<String, Double>();
            for (String a : Player.getPlayers().keySet()) {
                unsortedMap.put(a, statistic.getForPlayer(a));
            }
            Map<String, Double> sortedMap = Utils.sortByValue(unsortedMap);
            LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(sortedMap.entrySet());
            if (place < 1 || place > list.size()) {
                return "0";
            }
            if (type.equalsIgnoreCase("name")) {
                return Player.getName((String)((Map.Entry)list.get(place - 1)).getKey());
            }
            if (type.equalsIgnoreCase("value")) {
                DecimalFormat formatter = statistic == Statistic.WLR || statistic == Statistic.KDR ? new DecimalFormat("#0.00") : new DecimalFormat("#0");
                return formatter.format(((Map.Entry)list.get(place - 1)).getValue());
            }
        }
        if (params.startsWith("player_")) {
            Statistic statistic;
            String stat = params.split("_")[1].toUpperCase();
            try {
                statistic = Statistic.valueOf(stat);
            }
            catch (Exception ex) {
                return "0";
            }
            if (ID2 == null || !Player.isPlayer(ID2)) {
                return "0";
            }
            double value = statistic.getForPlayer(ID2);
            if (statistic == Statistic.WLR || statistic == Statistic.KDR) {
                return String.valueOf(value);
            }
            return String.valueOf((int)value);
        }
        return "0";
    }
}

