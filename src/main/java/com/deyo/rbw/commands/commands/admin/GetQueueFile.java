/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.admin;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.utils.FileUpload;

public class GetQueueFile
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            String ID2 = args2[1].replaceAll("[^0-9]", "");
            VoiceChannel target = null;
            try {
                target = g2.getVoiceChannelById(ID2);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (target != null) {
                BetterEmbed success = new BetterEmbed("success", "", "", "Here's the queue file", "");
                success.reply(msg);
                c.sendFiles(FileUpload.fromData(new File("RBW/queues/" + ID2 + ".yml"))).queue();
            } else {
                BetterEmbed.error(Messages.INVALID_VC).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

