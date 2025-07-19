/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyKick {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (!Parties.HasParty(m3.getId())) {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You don't own a party.", "");
            embed.reply(msg);
            return;
        }
        if (args2.length == 3) {
            Member member = Utils.getArg(args2[2], g2);
            if (member != null) {
                String ID2 = member.getId();
                if (Parties.kick(m3.getId(), ID2)) {
                    BetterEmbed embed = new BetterEmbed("success", "Player kicked", Utils.avatar(m3.getUser().getAvatarUrl()), "<@" + ID2 + "> was kicked from your party", "");
                    embed.reply(msg);
                } else {
                    BetterEmbed embed = new BetterEmbed("error", "Error", "", "The specified user couldn't get kicked from your party", "");
                    embed.reply(msg);
                }
            } else {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to kick from your party!", "");
                embed.reply(msg);
            }
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to kick from your party!", "");
            embed.reply(msg);
        }
    }
}

