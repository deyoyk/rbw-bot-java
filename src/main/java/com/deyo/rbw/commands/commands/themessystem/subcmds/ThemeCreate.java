/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.themessystem.subcmds;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ThemeCreate {
    public static void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        List<Message.Attachment> attachments = BetterEmbed.getAttachments(msg);
        if (args2.length == 3 && attachments.size() == 1) {
            if (!Utils.isLong(args2[2])) {
                BetterEmbed success = new BetterEmbed("error", "Error", "", "The specified cost is not a number", "");
                success.reply(msg);
                return;
            }
            if (new File("RBW/themes/" + attachments.get(0).getFileName()).exists()) {
                BetterEmbed error = new BetterEmbed("error", "", "", Messages.THEME_ALREADY_EXISTS.get(), "");
                error.reply(msg);
                return;
            }
            Themes.addTheme(attachments.get(0).getFileName(), Long.parseLong(args2[2]));
            attachments.get(0).downloadToFile("RBW/themes/" + attachments.get(0).getFileName());
            BetterEmbed success = new BetterEmbed("success", "Success", "", "Successfully added `" + attachments.get(0).getFileName().split("\\.")[0] + "` theme with cost " + args2[2], "Set cost to role id to make theme exclusive for a role!");
            success.setImage(attachments.get(0).getUrl());
            success.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", "theme create <cost> with 1 image attached, set cost as a role ID if you want to make the pack exclusive!"), "");
            error.reply(msg);
        }
    }
}

