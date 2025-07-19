/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.bukkit.Bukkit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

public class List
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        String description = "There are currently **" + Bukkit.getOnlinePlayers().size() + " players** online on the server\n\n";
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            String pID = Player.getIdFromIGN(player.getName());
            if (pID == null) {
                description = description + "- `" + player.getName() + "`\n";
                continue;
            }
            description = description + "- `" + player.getName() + "` (<@" + pID + ">)\n";
        }
        BetterEmbed reply = new BetterEmbed("info", "\uD83D\uDC65  Online Players", "", description, "");
        reply.reply(msg);
    }
}

