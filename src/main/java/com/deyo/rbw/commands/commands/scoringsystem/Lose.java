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

public class Lose
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 2) {
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
            int times = 1;
            if (args2.length > 2) {
                times = Integer.parseInt(args2[2].replaceAll("[^0-9]", ""));
            }
            int oldElo = (int)Statistic.ELO.getForPlayer(ID2);
            for (int i = 0; i < times; ++i) {
                Player.lose(ID2, g2, 0, null);
            }
            int newElo = (int)Statistic.ELO.getForPlayer(ID2);
            int change = newElo - oldElo;
            BetterEmbed embed = new BetterEmbed("success", "", "", "Gave " + member.getAsMention() + " +" + times + " losses with a change of " + change + " elo", "");
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

