/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import java.util.ArrayList;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyJoin {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        int elocap;
        int allelo;
        if (Parties.isInParty(m3.getId())) {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You are already in a party, to leave your current party type **=party leave**", "");
            embed.reply(msg);
            return;
        }
        String PartyOwner = Parties.partyinvites.getOrDefault(m3.getId(), null);
        if (PartyOwner == null) {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You don't have any incoming invite.", "");
            embed.reply(msg);
            return;
        }
        int maxPlayers = Config.getConfig().getInt("party-limit");
        if (Parties.PartyLength(PartyOwner) >= maxPlayers) {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "A max of " + maxPlayers + " players can be into a party.", "");
            embed.reply(msg);
            return;
        }
        ArrayList<String> memb = new ArrayList<String>();
        if (Parties.parties.containsKey(PartyOwner)) {
            memb = Parties.parties.get(PartyOwner);
        } else {
            memb.add(PartyOwner);
        }
        memb.add(m3.getId());
        if (Config.getConfig().getBoolean("elo-cap-enabled") && (allelo = Utils.CountElo(memb)) > (elocap = Config.getConfig().getInt("elo-cap"))) {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "The max elo of your party is " + elocap + ".", "");
            embed.reply(msg);
            return;
        }
        Parties.createParty(PartyOwner, memb);
        Parties.removeInvite(m3.getId());
        BetterEmbed embed = new BetterEmbed("success", "Joined Party", Utils.avatar(m3.getUser().getAvatarUrl()), m3.getAsMention() + " has joined <@" + PartyOwner + ">'s party.", "");
        embed.reply(msg);
    }
}

