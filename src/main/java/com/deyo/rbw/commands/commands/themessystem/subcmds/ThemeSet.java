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

public class ThemeSet {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 3) {
            Themes theme = Themes.getTheme(args2[2]);
            if (theme != null) {
                String[] tha;
                String th = theme.getName();
                ArrayList<String> OwnedThemes = new ArrayList<String>();
                for (String themes : tha = Player.getOwnedThemes(m3.getId()).split(",")) {
                    Themes t = Themes.getTheme(themes);
                    if (t == null) continue;
                    OwnedThemes.add(t.getName());
                }
                if (OwnedThemes.contains(th)) {
                    Player.setTheme(m3.getId(), theme.getName());
                    BetterEmbed embed = new BetterEmbed("success", "Done!", "", "You successfully set " + Utils.formatName(args2[2]) + " as current theme", "");
                    embed.reply(msg);
                } else {
                    BetterEmbed error = new BetterEmbed("error", "", "", "You don't have this theme!", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this theme!", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme set <theme you own>"), "");
            error.reply(msg);
        }
    }
}

