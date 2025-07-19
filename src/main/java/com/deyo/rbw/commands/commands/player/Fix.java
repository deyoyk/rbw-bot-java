/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Fix
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 1 || args2.length == 2) {
            String ID2 = args2.length == 2 ? args2[1].replaceAll("[^0-9]", "") : m3.getId();
            Player.fix(ID2, g2);
            BetterEmbed embed = new BetterEmbed("info", "\uD83D\uDD27  Player Fixed", "", "Roles and nickname for **" + Player.getName(ID2) + "** have been synced.", "");
            try {
                File banFile = new File("RBW/bans/" + ID2 + ".yml");
                if (banFile.exists()) {
                    YamlConfiguration banData = YamlConfiguration.loadConfiguration(banFile);
                    long unbanned = banData.getLong("unbanned");
                    if (unbanned < System.currentTimeMillis()) {
                        Player.unban(g2, ID2);
                        embed.setDescription("> You got unbanned");
                        BetterEmbed banembed = new BetterEmbed("success", "Ranked BedWars Moderation", Utils.avatar(g2.getIconUrl()), "You have been unbanned, Feel free to play now!", "");
                        banembed.addField("User", "<@" + ID2 + ">", true);
                        banembed.addField("Moderator", g2.getJDA().getSelfUser().getAsMention(), true);
                        banembed.addField("Reason", "`Ban Expired`", true);
                        if (RBW.banChannel != null) {
                            RBW.banChannel.sendMessage("<@" + ID2 + ">").queue();
                            RBW.banChannel.sendMessageEmbeds(banembed.build(), new MessageEmbed[0]).queue();
                        }
                    } else {
                        embed.setDescription("> You are banned until <t:" + unbanned / 1000L + ":F> ");
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().toString().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

