/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.themessystem;

import java.io.IOException;
import java.util.ArrayList;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeBuy;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeCreate;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeDelete;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeGive;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeInfo;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeList;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeRemove;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeReprice;
import com.deyo.rbw.commands.commands.themessystem.subcmds.ThemeSet;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Theme
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 1) {
            String footer = "";
            ArrayList<String> collections = Themes.getCollections();
            collections.sort(String::compareToIgnoreCase);
            Object description = "";
            for (String theme : collections) {
                if (((String)(description = (String)description + "\n\u2022 **" + Utils.formatName(theme) + "** \u2014 Collection")).length() <= 1700) continue;
                footer = "Themes exceed the text limit, please use info instead";
                break;
            }
            description = (String)description + "\n**Your Gold:** " + Statistic.GOLD.getForPlayer(m3.getId());
            description = (String)description + "\n*To check preview about a specific collection, run =theme list <collection>*";
            BetterEmbed embed = new BetterEmbed("success", "Themes", "", (String)description, footer);
            embed.reply(msg);
            return;
        }
        boolean f = g2.getRoles().contains(RBW.themesManagerRole);
        if (args2[1].equalsIgnoreCase("help")) {
            BetterEmbed embed = new BetterEmbed("success", "Help", "", "=theme list\n**Show a detailed list of themes**\n=theme set (theme) \n**Set a theme to your used ones**\n=theme buy (theme)\n**Buy a theme using gold**\n=theme info (theme)\n**View preview of a theme**", "");
            embed.reply(msg);
            return;
        }
        if (args2[1].equalsIgnoreCase("list")) {
            ThemeList.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("buy")) {
            ThemeBuy.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("info")) {
            ThemeInfo.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("set")) {
            ThemeSet.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("create") && f) {
            ThemeCreate.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("remove") && f) {
            ThemeRemove.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("delete") && f) {
            ThemeDelete.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("give") && f) {
            ThemeGive.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        if (args2[1].equalsIgnoreCase("reprice") && f) {
            ThemeReprice.doCMD(args2, g2, m3, c, msg, "");
            return;
        }
        Themes theme = Themes.getTheme(args2[1]);
        if (theme != null) {
            BetterEmbed.reply(theme.getThemeImage(), msg);
            BetterEmbed embed = new BetterEmbed("success", Utils.formatName(args2[1]) + " theme", "", "**Theme cost:** " + theme.getCost() + " Gold\n**Your gold:** " + Statistic.GOLD.getForPlayer(m3.getId()), "");
            c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            return;
        }
        BetterEmbed embed = new BetterEmbed("success", "Help", "", "=theme list\n**Show a detailed list of themes**\n=theme set (theme) \n**Set a theme to your used ones**\n=theme buy (theme)\n**Buy a theme using gold**\n=theme info (theme)\n**View preview of a theme**", "");
        embed.reply(msg);
    }
}

