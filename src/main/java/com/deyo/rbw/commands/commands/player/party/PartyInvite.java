/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import java.util.Collections;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class PartyInvite {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 3) {
            Member member = Utils.getArg(args2[2], g2);
            if (member != null) {
                String ID2 = member.getId();
                if (Parties.isInParty(ID2)) {
                    BetterEmbed embed = new BetterEmbed("error", "Error", "", "This player is already in a party!", "");
                    embed.reply(msg);
                    return;
                }
                if (ID2.equalsIgnoreCase(m3.getId())) {
                    BetterEmbed embed = new BetterEmbed("error", "Error", "", "You cannot invite yourself", "");
                    embed.reply(msg);
                    return;
                }
                Parties.addInvite(ID2, m3.getId());
                Button accept = Button.primary("accept-" + ID2, "Accept Party");
                BetterEmbed embed = new BetterEmbed("success", "Party invite", Utils.avatar(member.getUser().getAvatarUrl()), m3.getAsMention() + " invited the player " + member.getAsMention() + " to his party!\nType **=party join <@" + m3.getId() + ">** to accept his request!", "The invite will expire in 15 minutes");
                embed.replyWithButtons(Collections.singletonList(accept), msg);
            } else {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to invite to your party!", "");
                embed.reply(msg);
            }
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to invite to your party!", "");
            embed.reply(msg);
        }
    }
}

