/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GuildJoin {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        BetterEmbed embed;
        if (com.deyo.rbw.classes.guild.Guild.getGuildByMember(m3.getId()) != null) {
            BetterEmbed error = new BetterEmbed("error", "Error", "", "You are already in a guild. You can't accept this invite.", "");
            error.reply(msg);
            return;
        }
        com.deyo.rbw.classes.guild.Guild guild = null;
        block0: for (com.deyo.rbw.classes.guild.Guild g1 : com.deyo.rbw.classes.guild.Guild.guilds) {
            for (Member invitedMember : g1.getInvitedMembers()) {
                if (!invitedMember.getId().equalsIgnoreCase(m3.getId())) continue;
                guild = g1;
                continue block0;
            }
        }
        if (guild == null) {
            BetterEmbed embed2 = new BetterEmbed("error", "Error", "", "You don't have any incoming guild invite.", "");
            embed2.reply(msg);
            return;
        }
        int maxPlayers = Config.getConfig().getInt("guilds.max-players");
        if (guild.getMembers().size() >= maxPlayers) {
            embed = new BetterEmbed("error", "Error", "", "A max of " + maxPlayers + " players can be into a guild. This guild is already full!", "");
            embed.reply(msg);
            return;
        }
        guild.addMember(m3.getId());
        embed = new BetterEmbed("success", "Joined Guild", Utils.avatar(m3.getUser().getAvatarUrl()), m3.getAsMention() + " has joined " + guild.getGuildName() + ".", "");
        embed.reply(msg);
    }
}

