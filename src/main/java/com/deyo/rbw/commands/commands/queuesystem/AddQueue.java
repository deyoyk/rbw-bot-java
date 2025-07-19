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
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;

public class AddQueue
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 5) {
            VoiceChannel vc = null;
            String ID2 = args2[1].replaceAll("[^0-9]", "");
            try {
                vc = g2.getVoiceChannelById(ID2);
            }
            catch (Exception exception) {
                // empty catch block
            }
            Queue.QueueMaps map = Queue.QueueMaps.Quads;
            if (args2.length > 5) {
                try {
                    map = Queue.QueueMaps.valueOf(args2[5]);
                }
                catch (Exception ex) {
                    BetterEmbed embed = new BetterEmbed("error", "Error", "", "Specify a valid map type: `" + Queue.QueueMaps.Doubles.name() + " or " + Queue.QueueMaps.Quads.name() + "`", "");
                    embed.reply(msg);
                    return;
                }
            }
            String casual = args2[4].toLowerCase();
            if (vc != null) {
                int number = Integer.parseInt(args2[2]);
                if (number > 0) {
                    File vcfile = new File("RBW/queues/" + vc.getId() + ".yml");
                    if (!vcfile.exists()) {
                        if (args2[3].equalsIgnoreCase("captains") || args2[3].equalsIgnoreCase("automatic")) {
                            if (casual.equals("true") || casual.equals("false")) {
                                Queue.addQueue(ID2, number, args2[3], Boolean.parseBoolean(casual), map);
                                BetterEmbed embed = new BetterEmbed("success", "\u2705 successfully added `" + vc.getName() + "` queue", "", "", "");
                                embed.addField("VC", vc.getAsMention(), true);
                                embed.addField("Players in each team:", args2[2], true);
                                embed.addField("Sorting mode:", args2[3], true);
                                embed.addField("Casual queue:", casual, true);
                                ((VoiceChannelManager)vc.getManager().setUserLimit(99)).queue();
                                embed.reply(msg);
                            } else {
                                BetterEmbed.error(Messages.QUEUE_CASUAL).reply(msg);
                            }
                        } else {
                            BetterEmbed.error(Messages.QUEUE_INVALID_MODE).reply(msg);
                        }
                    } else {
                        BetterEmbed.error(Messages.QUEUE_ALREADY_EXISTS).reply(msg);
                    }
                } else {
                    BetterEmbed.error(Messages.QUEUE_INVALID_PLAYERS).reply(msg);
                }
            } else {
                BetterEmbed.error(Messages.INVALID_VC).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

