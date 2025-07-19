/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.themessystem.subcmds;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeInfo {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 3) {
            Themes theme = Themes.getTheme(args2[2]);
            if (theme != null) {
                BetterEmbed.reply(theme.getThemeImage(), msg);
                BetterEmbed embed = new BetterEmbed("success", Utils.formatName(args2[2]) + " theme", "", "**Theme cost:** " + theme.getCost() + " Gold\n**Your gold:** " + Statistic.GOLD.getForPlayer(m3.getId()), "");
                c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this theme!", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme info <theme>"), "");
            error.reply(msg);
        }
    }
}

