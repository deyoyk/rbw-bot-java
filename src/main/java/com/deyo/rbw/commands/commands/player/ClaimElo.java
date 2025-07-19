/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.bukkit.Bukkit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

public class ClaimElo
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        BetterEmbed error;
        int games = (int)Statistic.GAMES.getForPlayer(m3.getId());
        int elo = (int)Statistic.ELO.getForPlayer(m3.getId());
        if (games > 0 || elo > 0) {
            BetterEmbed error2 = new BetterEmbed("error", "", "", "⚠️ You can only claim Elo if you haven't played any games this season.", "");
            error2.reply(msg);
            return;
        }
        HashMap<Role, Integer> claimElo = new HashMap<Role, Integer>();
        for (Map.Entry<String, Object> e : Config.getConfig().getConfigurationSection("claim-elo").getValues(false).entrySet()) {
            Role r = g2.getRoleById(e.getKey());
            int claimelo = Config.getConfig().getInt("claim-elo." + e.getKey());
            if (r == null) {
                Bukkit.getConsoleSender().sendMessage("\u00a7cCan't find the role with ID " + e.getKey());
                Bukkit.getConsoleSender().sendMessage("\u00a7cCLAIM ELO ERROR");
                continue;
            }
            claimElo.put(r, claimelo);
        }
        Object desc = "**You don't have any eligible claim-Elo roles:**\n\n";
        IMentionable highestRole = null;
        int claimelo = 0;
        for (Map.Entry e : claimElo.entrySet()) {
            desc = (String)desc + ((Role)e.getKey()).getAsMention() + ": " + e.getValue() + " Elo\n";
            if (highestRole == null && m3.getRoles().contains(e.getKey())) {
                highestRole = (Role)e.getKey();
                claimelo = (Integer)e.getValue();
                continue;
            }
            if (highestRole == null || !m3.getRoles().contains(e.getKey()) || claimelo >= (Integer)e.getValue()) continue;
            highestRole = (Role)e.getKey();
            claimelo = (Integer)e.getValue();
        }
        if (highestRole == null) {
            error = new BetterEmbed("error", "", "", (String)desc, "");
            error.reply(msg);
            return;
        }
        Statistic.ELO.setForPlayer(m3.getId(), Statistic.ELO.getForPlayer(m3.getId()) + (double)claimelo);
        error = new BetterEmbed("info", "\uD83D\uDCCA  Elo Claimed", "", "You have successfully claimed startup Elo for role " + highestRole.getAsMention() + ": **+" + claimelo + " Elo**.", "");
        error.reply(msg);
    }
}

