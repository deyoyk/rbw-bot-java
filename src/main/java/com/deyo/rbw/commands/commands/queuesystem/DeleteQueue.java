/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.queuesystem;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class DeleteQueue
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            String ID2 = args2[1].replaceAll("[^0-9]", "");
            File vcfile = new File("RBW/queues/" + ID2 + ".yml");
            if (vcfile.exists()) {
                Queue.delQueue(ID2);
                BetterEmbed embed = new BetterEmbed("success", "", "", Messages.QUEUE_DELETED.get(), "");
                embed.reply(msg);
            } else {
                BetterEmbed.error(Messages.INVALID_QUEUE).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

