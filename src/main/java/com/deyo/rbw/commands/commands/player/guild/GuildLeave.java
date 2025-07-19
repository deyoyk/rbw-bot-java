/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GuildLeave {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        com.deyo.rbw.classes.guild.Guild guild = com.deyo.rbw.classes.guild.Guild.getGuildByMember(m3.getId());
        if (guild == null) {
            BetterEmbed error = new BetterEmbed("error", "Error", "", "You are currently not in a guild.", "");
            error.reply(msg);
            return;
        }
        if (guild.getLeader().equalsIgnoreCase(m3.getId())) {
            BetterEmbed error = new BetterEmbed("error", "Error", "", "You are the guild leader. Please use =guild disband to disband the guild!", "");
            error.reply(msg);
            return;
        }
        if (guild.getOfficers().contains(m3.getId())) {
            guild.removeOfficer(m3.getId());
        }
        guild.removeMember(m3.getId());
        BetterEmbed error = new BetterEmbed("success", "Success", "", "You left the guild **" + guild.getGuildName() + "**!", "");
        error.reply(msg);
    }
}

