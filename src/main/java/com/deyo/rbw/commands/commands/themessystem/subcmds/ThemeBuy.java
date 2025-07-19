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
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeBuy {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 3) {
            Themes theme = Themes.getTheme(args2[2]);
            if (theme != null) {
                BetterEmbed error;
                String[] tha;
                ArrayList<String> OwnedThemes = new ArrayList<String>();
                String th = theme.getName();
                for (String themes : tha = Player.getOwnedThemes(m3.getId()).split(",")) {
                    Themes t = Themes.getTheme(themes);
                    if (t == null) continue;
                    OwnedThemes.add(t.getName());
                }
                if (OwnedThemes.contains(th)) {
                    BetterEmbed error2 = new BetterEmbed("error", "", "", "It seems like you already have " + theme.getName() + " theme!\n To set it as your current one, run =theme set (theme)", "");
                    error2.reply(msg);
                    return;
                }
                int gold = (int)Statistic.GOLD.getForPlayer(m3.getId());
                long cost = theme.getCost();
                Role r = g2.getRoleById(cost);
                if (r != null) {
                    if (m3.getRoles().contains(r)) {
                        error = new BetterEmbed("success", "", "", "You successfully now have " + theme.getName() + " free!\n To set it as your current one, run =theme set (theme)", "");
                        error.reply(msg);
                        Player.addTheme(m3.getId(), theme.getName());
                        return;
                    }
                    error = new BetterEmbed("error", "", "", "This theme is exclusive for " + r.getName(), "");
                    error.reply(msg);
                    return;
                }
                if (cost <= (long)gold) {
                    long diff = (long)gold - cost;
                    Statistic.GOLD.setForPlayer(m3.getId(), diff);
                    BetterEmbed error3 = new BetterEmbed("success", "", "", "You successfully purchaesed " + theme.getName() + " as theme!\n To set it as your current one, run =theme set (theme)", "");
                    error3.reply(msg);
                    Player.addTheme(m3.getId(), theme.getName());
                } else {
                    error = new BetterEmbed("error", "", "", "You don't have enough gold!", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this theme!", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme buy <theme>"), "");
            error.reply(msg);
        }
    }
}

