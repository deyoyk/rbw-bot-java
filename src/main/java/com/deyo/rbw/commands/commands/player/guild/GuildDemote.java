/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GuildDemote {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 3) {
            com.deyo.rbw.classes.guild.Guild guild = com.deyo.rbw.classes.guild.Guild.getGuildByMember(m3.getId());
            if (guild == null) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "You are currently not in a guild.", "");
                error.reply(msg);
                return;
            }
            if (!guild.getLeader().equalsIgnoreCase(m3.getId())) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Only the leader can demote members of the guild!", "");
                embed.reply(msg);
                return;
            }
            Member member = Utils.getArg(args2[2], g2);
            if (member == null) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid member to demote.", "");
                embed.reply(msg);
                return;
            }
            if (!guild.getOfficers().contains(member.getId())) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "This player is not officer.", "");
                embed.reply(msg);
                return;
            }
            guild.removeOfficer(member.getId());
            BetterEmbed embed = new BetterEmbed("success", "Success", "", "Successfully demoted " + member.getAsMention() + " from **Guild Officer**", "");
            embed.reply(msg);
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid player to demote from the guild!", "");
            embed.reply(msg);
        }
    }
}

