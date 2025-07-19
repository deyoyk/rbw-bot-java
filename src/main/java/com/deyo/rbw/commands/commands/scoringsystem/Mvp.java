/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.scoringsystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Mvp
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String mvp = member.getId();
            Rank r = Rank.getPlayerRank(mvp);
            if (r == null) {
                BetterEmbed error = new BetterEmbed("error", "", "", "An error occured while trying to calculate player's rank.", "");
                error.reply(msg);
                return;
            }
            BetterEmbed embed = new BetterEmbed("success", "Done", "", "Gave mvp to " + member.getAsMention() + "\n", "");
            Statistic.MVP.setForPlayer(mvp, Statistic.MVP.getForPlayer(mvp) + 1.0);
            Statistic.ELO.setForPlayer(mvp, Statistic.ELO.getForPlayer(mvp) + (double)r.getMvpElo());
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

