/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GuildCreate {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 3) {
            int minGold = Config.getConfig().getInt("guilds.min-gold", 0);
            if (Statistic.GOLD.getForPlayer(m3.getId()) < (double)minGold) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "You don't have enough gold to create a guild, You need a minimum of `" + minGold + "` gold", "");
                error.reply(msg);
                return;
            }
            if (com.deyo.rbw.classes.guild.Guild.getGuildByMember(m3.getId()) != null) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "You are already in a guild, Please leave your current guild.", "");
                error.reply(msg);
                return;
            }
            String guildName = args2[2];
            if (com.deyo.rbw.classes.guild.Guild.getGuildByName(guildName) != null) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "A guild with that name already exists.", "");
                error.reply(msg);
                return;
            }
            if (guildName.length() > 16) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "A guild name can be 16 characters max.", "");
                error.reply(msg);
                return;
            }
            new com.deyo.rbw.classes.guild.Guild(guildName, m3.getId());
            Statistic.GOLD.setForPlayer(m3.getId(), Statistic.GOLD.getForPlayer(m3.getId()) - (double)minGold);
            BetterEmbed embed = new BetterEmbed("success", "Guild System", "", "You successfully created a guild named `" + guildName + "`.", "");
            embed.reply(msg);
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "Please specify a valid name to create the guild!", "");
            embed.reply(msg);
        }
    }
}

