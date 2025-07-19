/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.queuesystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Queues
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 1) {
            Object str = "";
            for (Queue queue : Queue.getQueues()) {
                VoiceChannel vc = g2.getVoiceChannelById(queue.getId());
                if (vc == null) {
                    str = (String)str + "`" + queue.getId() + "` `No channel.`\n";
                    continue;
                }
                str = (String)str + "<#" + queue.getId() + "> `" + vc.getMembers().size() + "/" + queue.getPlayersInTeam() * 2 + "`, `" + queue.getPicking() + "` `" + queue.getMapUsed().name() + "` \n";
            }
            BetterEmbed embed = new BetterEmbed("success", "Queue list", "", "This also shows **currently** queueing players\n" + (String)str, "");
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

