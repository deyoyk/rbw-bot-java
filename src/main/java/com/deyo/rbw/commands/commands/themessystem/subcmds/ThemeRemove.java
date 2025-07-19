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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeRemove {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 4) {
            Themes theme = Themes.getTheme(args2[3]);
            if (theme != null) {
                Member member = Utils.getArg(args2[2], g2);
                if (member != null) {
                    String[] tha;
                    String th = theme.getName();
                    ArrayList<String> OwnedThemes = new ArrayList<String>();
                    for (String themes : tha = Player.getOwnedThemes(m3.getId()).split(",")) {
                        Themes t = Themes.getTheme(themes);
                        if (t == null) continue;
                        OwnedThemes.add(t.getName());
                    }
                    if (OwnedThemes.contains(th)) {
                        Player.removeTheme(m3.getId(), theme.getName());
                        BetterEmbed embed = new BetterEmbed("success", "Done!", "", "You successfully removed " + Utils.formatName(args2[2]) + " from his themes", "");
                        embed.reply(msg);
                    } else {
                        BetterEmbed error = new BetterEmbed("error", "", "", "You don't have this theme!", "");
                        error.reply(msg);
                    }
                } else {
                    BetterEmbed error = new BetterEmbed("error", "Error", "", "Please specify a valid member", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this theme!", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme remove <player> <theme>"), "");
            error.reply(msg);
        }
    }
}

