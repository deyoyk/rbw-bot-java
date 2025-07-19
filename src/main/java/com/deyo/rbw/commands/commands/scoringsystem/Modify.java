/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.scoringsystem;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Modify
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 4) {
            try {
                int amount;
                Member member;
                Object content = "";
                for (String arg : args2) {
                    content = (String)content + arg + " ";
                }
                BetterEmbed embed1 = new BetterEmbed("default", "Command sent", "", "**" + (String)content + "**\nBy " + m3.getAsMention(), "");
                if (RBW.scoringLogsChannel != null) {
                    RBW.scoringLogsChannel.sendMessageEmbeds(embed1.build(), new MessageEmbed[0]).complete();
                }
                if ((member = Utils.getArg(args2[1], g2)) == null) {
                    BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                    return;
                }
                String ID2 = member.getId();
                String stat = args2[2];
                try {
                    amount = Integer.parseInt(args2[3]);
                }
                catch (Exception ex) {
                    BetterEmbed embed = new BetterEmbed("success", "Error", "", "The specified amount is not a number.", "");
                    embed.reply(msg);
                    return;
                }
                BetterEmbed embed = new BetterEmbed("success", "Modified " + Player.getName(ID2) + "'s Stats", "", "", "");
                Statistic statistic = Statistic.getFromString(stat);
                if (statistic == null) {
                    Object s2 = "";
                    int maxv = Statistic.values().length - 1;
                    for (int i = 0; i < Statistic.values().length; ++i) {
                        Statistic value = Statistic.values()[i];
                        s2 = i == maxv ? (String)s2 + "`" + value.getPath() + "`" : (String)s2 + "`" + value.getPath() + "`/";
                    }
                    BetterEmbed error = new BetterEmbed("error", "Unknown Stat", "", "Must be " + (String)s2, "");
                    error.reply(msg);
                    return;
                }
                int oldstat = (int)statistic.getForPlayer(ID2);
                statistic.setForPlayer(ID2, oldstat + amount);
                embed.addField("player's " + statistic.getPath(), oldstat + " > " + (int)statistic.getForPlayer(ID2), true);
                Player.fix(ID2, g2);
                embed.reply(msg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

