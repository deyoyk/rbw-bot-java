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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeReprice {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 4) {
            Themes theme = Themes.getTheme(args2[2]);
            if (theme != null) {
                if (!Utils.isLong(args2[3])) {
                    BetterEmbed success = new BetterEmbed("error", "Error", "", "The specified cost is not a number", "");
                    success.reply(msg);
                    return;
                }
                Themes.removeTheme(theme.getFile());
                Themes.addTheme(theme.getName(), Long.parseLong(args2[3]));
                BetterEmbed embed = new BetterEmbed("success", "Done!", "", "You successfully changed price of this theme", "");
                embed.reply(msg);
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "Couldn't find this theme!", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme reprice <theme> <newCost>"), "");
            error.reply(msg);
        }
    }
}

