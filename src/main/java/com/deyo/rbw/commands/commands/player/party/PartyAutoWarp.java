/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.commands.CommandManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyAutoWarp {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (!CommandManager.checkPerms("party-autowarp", m3, g2)) {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You don't have permission to run that command.", "");
            embed.reply(msg);
            return;
        }
        if (Parties.HasParty(m3.getId())) {
            String change;
            if (Parties.autoWarps.contains(m3.getId())) {
                change = "off";
                Parties.autoWarps.remove(m3.getId());
            } else {
                change = "on";
                Parties.autoWarps.add(m3.getId());
            }
            BetterEmbed embed = new BetterEmbed("success", "Auto Warp", "", "You turned party auto warp " + change, "");
            embed.reply(msg);
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You don't own a party.", "");
            embed.reply(msg);
        }
    }
}

