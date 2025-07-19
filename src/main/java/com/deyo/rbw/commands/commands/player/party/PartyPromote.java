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

public class PartyPromote {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (Parties.HasParty(m3.getId())) {
            Member member = Utils.getArg(args2[2], g2);
            if (member != null) {
                String ID2 = member.getId();
                Parties.promote(ID2, m3.getId());
                BetterEmbed embed = new BetterEmbed("success", "Party promote", Utils.avatar(member.getUser().getAvatarUrl()), "You promoted " + member.getAsMention() + " as party owner.", "");
                embed.reply(msg);
            } else {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to promote as your party owner.", "");
                embed.reply(msg);
            }
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You don't own a party.", "");
            embed.reply(msg);
        }
    }
}

