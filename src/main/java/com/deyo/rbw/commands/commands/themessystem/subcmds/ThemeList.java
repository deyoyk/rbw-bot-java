/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.themessystem.subcmds;

import java.io.IOException;
import java.util.ArrayList;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeList {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 2) {
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
        } else if (args2.length == 3) {
            String collection = args2[2];
            if (!Themes.isCollection(collection)) {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this collection!", "");
                error.reply(msg);
                return;
            }
            String footer = "";
            ArrayList<Themes> themes = Themes.list(collection);
            Object description = "";
            for (Themes theme : themes) {
                long cost = theme.getCost();
                String name = theme.getName();
                Role r = g2.getRoleById(cost);
                description = r == null ? (String)description + "\n\u2022 **" + Utils.formatName(name) + "** \u2014 __Cost__: " + cost + " Gold" : (String)description + "\n\u2022 **" + Utils.formatName(name) + "** \u2014 Exclusive for role " + r.getName();
                if (((String)description).length() <= 1700) continue;
                footer = "Themes exceed the text limit, please use info instead";
                break;
            }
            description = (String)description + "\n\n**Your Owned Themes:** " + Player.getOwnedThemes(m3.getId());
            description = (String)description + "\n**Your Gold:** " + Statistic.GOLD.getForPlayer(m3.getId());
            description = (String)description + "\n*To check preview about a specific theme, run =theme info <theme>*";
            BetterEmbed embed = new BetterEmbed("success", "Themes", "", (String)description, footer);
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme list"), "");
            error.reply(msg);
        }
    }
}

