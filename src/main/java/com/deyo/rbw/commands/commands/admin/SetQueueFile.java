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
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class SetQueueFile
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            List<Message.Attachment> attachments = BetterEmbed.getAttachments(msg);
            if (attachments.size() == 1) {
                if (attachments.get(0).getFileName().contains(".yml")) {
                    String ID2 = args2[1].replaceAll("[^0-9]", "");
                    VoiceChannel target = null;
                    try {
                        target = g2.getVoiceChannelById(ID2);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (target != null) {
                        attachments.get(0).downloadToFile("RBW/queues/" + ID2 + ".yml");
                        BetterEmbed success = new BetterEmbed("success", "", "", "Done! I set the queue file.", "");
                        success.reply(msg);
                    } else {
                        BetterEmbed.error(Messages.INVALID_VC).reply(msg);
                    }
                } else {
                    BetterEmbed error = new BetterEmbed("error", "", "", "Wrong queue file!", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "No queue files attached..", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

