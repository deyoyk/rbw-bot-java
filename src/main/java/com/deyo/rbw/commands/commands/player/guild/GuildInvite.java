/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import java.util.Collections;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class GuildInvite {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 3) {
            com.deyo.rbw.classes.guild.Guild guild = com.deyo.rbw.classes.guild.Guild.getGuildByMember(m3.getId());
            if (guild == null) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "You are currently not in a guild.", "");
                error.reply(msg);
                return;
            }
            if (!guild.getOfficers().contains(m3.getId()) && !guild.getLeader().equalsIgnoreCase(m3.getId())) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "You need to be at least guild officer to invite players.", "");
                error.reply(msg);
                return;
            }
            Member member = Utils.getArg(args2[2], g2);
            if (member == null) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to invite to the guild!", "");
                embed.reply(msg);
                return;
            }
            String ID2 = member.getId();
            if (ID2.equalsIgnoreCase(m3.getId())) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "You cannot invite yourself", "");
                embed.reply(msg);
                return;
            }
            if (com.deyo.rbw.classes.guild.Guild.getGuildByMember(ID2) != null) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "This player is already in a guild!", "");
                embed.reply(msg);
                return;
            }
            guild.addInvitedMember(member);
            Button accept = Button.primary("acceptguild-" + ID2, "Accept Invite");
            BetterEmbed embed = new BetterEmbed("success", "Guild invite", Utils.avatar(member.getUser().getAvatarUrl()), m3.getAsMention() + " invited the player " + member.getAsMention() + " to " + guild.getGuildName() + " guild!\nType **=guild join " + guild.getGuildName() + "** to accept his request!", "The invite will expire in 15 minutes");
            embed.replyWithButtons(Collections.singletonList(accept), msg);
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid player to invite to the guild!", "");
            embed.reply(msg);
        }
    }
}

