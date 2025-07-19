/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import java.util.ArrayList;
import java.util.Map;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Parties;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyLeave {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (Parties.isInParty(m3.getId())) {
            BetterEmbed embed;
            String partyOwner = null;
            for (Map.Entry<String, ArrayList<String>> entry : Parties.parties.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> value = entry.getValue();
                if (!value.contains(m3.getId())) continue;
                partyOwner = key;
                break;
            }
            if (partyOwner != null && Parties.kick(partyOwner, m3.getId())) {
                embed = new BetterEmbed("success", "Done", "", "**You have left the party**", "");
                embed.reply(msg);
            } else {
                embed = new BetterEmbed("error", "Error", "", "You are not in a party.", "");
                embed.reply(msg);
            }
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You are not in a party.", "");
            embed.reply(msg);
        }
    }
}

