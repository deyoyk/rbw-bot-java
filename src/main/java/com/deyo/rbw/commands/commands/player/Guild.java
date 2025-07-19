/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.commands.player.guild.GuildCreate;
import com.deyo.rbw.commands.commands.player.guild.GuildDemote;
import com.deyo.rbw.commands.commands.player.guild.GuildDisband;
import com.deyo.rbw.commands.commands.player.guild.GuildInfo;
import com.deyo.rbw.commands.commands.player.guild.GuildInvite;
import com.deyo.rbw.commands.commands.player.guild.GuildJoin;
import com.deyo.rbw.commands.commands.player.guild.GuildKick;
import com.deyo.rbw.commands.commands.player.guild.GuildLeave;
import com.deyo.rbw.commands.commands.player.guild.GuildList;
import com.deyo.rbw.commands.commands.player.guild.GuildPromote;
import com.deyo.rbw.commands.commands.player.guild.GuildTransfer;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Guild
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, net.dv8tion.jda.api.entities.Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length > 1) {
            Member target = Utils.getArg(args2[1], g2);
            if (target != null) {
                GuildInvite.execute(new String[]{"=guild", "invite", target.getId()}, g2, m3, c, msg, "");
                return;
            }
            if (args2[1].equalsIgnoreCase("help")) {
                BetterEmbed embed = new BetterEmbed("info", "\uD83C\uDFE0  Guild Commands", "", "**=guild create <name>** – create guild\n**=guild invite <player>** – invite player\n**=guild join** – accept invite\n**=guild promote <player>** – make officer\n**=guild demote <player>** – demote officer\n**=guild transfer <player>** – new leader\n**=guild leave** – leave guild\n**=guild list** – list members\n**=guild disband** – delete guild\n**=guild kick <player>** – kick member\n**=guild info** – guild info", "");
                c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            } else if (args2[1].equalsIgnoreCase("create")) {
                GuildCreate.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("invite")) {
                GuildInvite.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("join")) {
                GuildJoin.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("transfer")) {
                GuildTransfer.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("promote")) {
                GuildPromote.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("demote")) {
                GuildDemote.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("leave")) {
                GuildLeave.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("list")) {
                GuildList.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("disband")) {
                GuildDisband.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("kick")) {
                GuildKick.execute(args2, g2, m3, c, msg, usage);
            } else if (args2[1].equalsIgnoreCase("info")) {
                GuildInfo.execute(args2, g2, m3, c, msg, usage);
            }
        } else {
            BetterEmbed embed = new BetterEmbed("info", "\uD83C\uDFE0  Guild Commands", "", "**=guild create <name>** – create guild\n**=guild invite <player>** – invite player\n**=guild join** – accept invite\n**=guild promote <player>** – make officer\n**=guild demote <player>** – demote officer\n**=guild transfer <player>** – new leader\n**=guild leave** – leave guild\n**=guild list** – list members\n**=guild disband** – delete guild\n**=guild kick <player>** – kick member\n**=guild info** – guild info", "");
            c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
        }
    }
}

