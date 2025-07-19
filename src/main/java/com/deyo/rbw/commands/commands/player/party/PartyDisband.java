/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Parties;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyDisband {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (Parties.HasParty(m3.getId())) {
            Parties.deleteParty(m3.getId());
            BetterEmbed embed = new BetterEmbed("success", "Disbanded", "", "Successfully disbanded your party", "");
            embed.reply(msg);
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You don't own a party.", "");
            embed.reply(msg);
        }
    }
}

