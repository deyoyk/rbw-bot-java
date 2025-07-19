/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GuildInfo {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        com.deyo.rbw.classes.guild.Guild guild = com.deyo.rbw.classes.guild.Guild.getGuildByMember(m3.getId());
        if (guild == null) {
            BetterEmbed error = new BetterEmbed("error", "Error", "", "You are currently not in a guild.", "");
            error.reply(msg);
            return;
        }
        BetterEmbed hi = new BetterEmbed("success", "Soon", "", "I'll add this command soon, just know it'll be there.", "");
        hi.reply(msg);
    }
}

