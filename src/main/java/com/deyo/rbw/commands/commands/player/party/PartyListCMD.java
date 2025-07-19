/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyListCMD
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        String ID2 = null;
        if (args2.length == 1) {
            if (Parties.HasParty(m3.getId())) {
                ID2 = m3.getId();
            } else if (Parties.isInParty(m3.getId())) {
                String partyOwner = null;
                for (Map.Entry<String, ArrayList<String>> entry : Parties.parties.entrySet()) {
                    String string = entry.getKey();
                    ArrayList<String> arrayList = entry.getValue();
                    if (!arrayList.contains(m3.getId())) continue;
                    partyOwner = string;
                    break;
                }
                ID2 = partyOwner;
            } else {
                ID2 = m3.getId();
            }
        } else {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to join his party.", "");
                embed.reply(msg);
                return;
            }
            if (Parties.HasParty(member.getId())) {
                ID2 = member.getId();
            } else if (Parties.isInParty(member.getId())) {
                String partyOwner = null;
                for (Map.Entry<String, ArrayList<String>> entry : Parties.parties.entrySet()) {
                    String string = entry.getKey();
                    ArrayList<String> value = entry.getValue();
                    if (!value.contains(member.getId())) continue;
                    partyOwner = string;
                    break;
                }
                ID2 = partyOwner;
            } else {
                ID2 = member.getId();
            }
        }
        if (Parties.HasParty(ID2)) {
            ArrayList<String> mbers = Parties.getPartyMembers(ID2);
            String description = "__Party Leader__: <@" + ID2 + ">\n\nMembers `[" + mbers.size() + "/" + Config.getValue("party-limit") + "]`:\n";
            for (String string : mbers) {
                description = description + "`\u2022` <@" + string + ">\n";
            }
            ArrayList<String> arrayList = new ArrayList<String>();
            for (Map.Entry<String, String> entry : Parties.partyinvites.entrySet()) {
                if (!entry.getValue().equalsIgnoreCase(ID2)) continue;
                arrayList.add(entry.getKey());
            }
            if (!arrayList.isEmpty()) {
                description = description + "\nInvited `[" + arrayList.size() + "]`:\n";
                for (String string : arrayList) {
                    description = description + "`\u2022`<@" + string + ">\n";
                }
            }
            description = description + "\nAuto Warp: `" + (Parties.autoWarps.contains(ID2) ? "On" : "Off") + "`";
            BetterEmbed betterEmbed = new BetterEmbed("success", "Party information", "", description, "");
            betterEmbed.reply(msg);
        } else {
            BetterEmbed embed;
            if (args2.length != 1) {
                embed = new BetterEmbed("error", "Error", "", "The specified player doesn't own a party.", "");
                embed.reply(msg);
                return;
            }
            embed = new BetterEmbed("error", "Error", "", "You don't own a party.", "");
            embed.reply(msg);
        }
    }
}

