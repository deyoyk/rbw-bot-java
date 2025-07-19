/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.commands.player.party.PartyAutoWarp;
import com.deyo.rbw.commands.commands.player.party.PartyDisband;
import com.deyo.rbw.commands.commands.player.party.PartyInvite;
import com.deyo.rbw.commands.commands.player.party.PartyJoin;
import com.deyo.rbw.commands.commands.player.party.PartyKick;
import com.deyo.rbw.commands.commands.player.party.PartyLeave;
import com.deyo.rbw.commands.commands.player.party.PartyList;
import com.deyo.rbw.commands.commands.player.party.PartyPromote;
import com.deyo.rbw.commands.commands.player.party.PartyWarp;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Party
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length > 1) {
            Member target = Utils.getArg(args2[1], g2);
            if (target != null) {
                PartyInvite.execute(new String[]{"=party", "invite", target.getId()}, g2, m3, c, msg, "");
            }
            if (args2[1].equalsIgnoreCase("invite") || args2[1].equalsIgnoreCase("add")) {
                PartyInvite.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("join") || args2[1].equalsIgnoreCase("enter")) {
                PartyJoin.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("warp")) {
                PartyWarp.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("leave")) {
                PartyLeave.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("list")) {
                PartyList.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("disband")) {
                PartyDisband.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("promote") || args2[1].equalsIgnoreCase("transfer")) {
                PartyPromote.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("autowarp")) {
                PartyAutoWarp.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("kick") || args2[1].equalsIgnoreCase("remove")) {
                PartyKick.execute(args2, g2, m3, c, msg, "");
            } else if (args2[1].equalsIgnoreCase("help")) {
                BetterEmbed embed = new BetterEmbed("info", "\uD83C\uDF89  Party Commands", "", "**=party invite <player>** – invite a player\n**=party promote <player>** – transfer ownership\n**=party leave** – leave your party\n**=party list** – list members\n**=party disband** – delete party\n**=party kick <player>** – remove member\n**=party warp <player>** – pull to your VC\n**=party join <player>** – accept invite\n**=party autowarp** – toggle auto-warp", "");
                c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

