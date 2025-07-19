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
import com.deyo.rbw.classes.player.Player;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeDelete {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 3) {
            Themes theme = Themes.getTheme(args2[2]);
            if (theme != null) {
                if (theme.getThemeImage() != null) {
                    theme.getThemeImage().delete();
                } else {
                    BetterEmbed.reply("image is null, contact dev.", msg);
                }
                Themes.removeTheme(theme.getFile());
                String th = theme.getName();
                for (String ID2 : Player.getPlayers().keySet()) {
                    if (Player.getTheme(ID2).contains(th)) {
                        Player.setTheme(ID2, "default");
                    }
                    if (!Player.getOwnedThemes(ID2).contains(th)) continue;
                    Player.removeTheme(ID2, th);
                }
                BetterEmbed success = new BetterEmbed("success", "Success", "", "Successfully deleted " + Utils.formatName(args2[2]), "");
                success.reply(msg);
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this theme!", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme delete <theme>"), "");
            error.reply(msg);
        }
    }
}

