/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.admin;

import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class SetPlayerFile
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        List<Message.Attachment> attachments = BetterEmbed.getAttachments(msg);
        if (args2.length == 2) {
            if (attachments.size() == 1) {
                if (attachments.get(0).getFileName().contains(".yml")) {
                    String ID2 = args2[1].replaceAll("[^0-9]", "");
                    Member target = Utils.getArg(args2[1], g2);
                    if (target != null) {
                        attachments.get(0).downloadToFile("RBW/players/" + ID2 + ".yml");
                        BetterEmbed success = new BetterEmbed("success", "", "", "Done, I set the player file", "");
                        success.reply(msg);
                    } else {
                        BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                    }
                } else {
                    BetterEmbed error = new BetterEmbed("error", "", "", "Invalid player file...", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "There were no files attached..", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

