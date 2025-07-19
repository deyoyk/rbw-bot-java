/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.admin;

import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class SetRankFile
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            List<Message.Attachment> attachments = BetterEmbed.getAttachments(msg);
            if (attachments.size() == 1) {
                if (attachments.get(0).getFileName().contains(".yml")) {
                    String ID2 = args2[1].replaceAll("[^0-9]", "");
                    Role target = null;
                    try {
                        target = g2.getRoleById(ID2);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (target != null) {
                        attachments.get(0).downloadToFile("RBW/ranks/" + ID2 + ".yml");
                        BetterEmbed success = new BetterEmbed("success", "", "", "Rank file set.", "");
                        success.reply(msg);
                    } else {
                        BetterEmbed.error(Messages.INVALID_ROLE).reply(msg);
                    }
                } else {
                    BetterEmbed error = new BetterEmbed("error", "", "", "Wrong rank file...", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "No rank file provided", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

