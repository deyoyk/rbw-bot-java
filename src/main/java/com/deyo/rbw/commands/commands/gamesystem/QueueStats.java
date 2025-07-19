/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class QueueStats
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 1) {
            if (Game.isGameChannel(c.getId())) {
                int number = Game.getNumber(c.getId());
                Game game = Game.getFromNumber(number);
                Object t1 = "";
                for (String string : game.getTeam(1)) {
                    t1 = (String)t1 + "\u2022 <@" + string + "> \u2014 `ELO: " + (int)Statistic.ELO.getForPlayer(string) + "` `" + (int)Statistic.WIN.getForPlayer(string) + "W/" + (int)Statistic.LOSSES.getForPlayer(string) + "L` `(" + Statistic.WLR.getForPlayer(string) + "WLR)`\n";
                }
                Object t2 = "";
                for (String s3 : game.getTeam(2)) {
                    t2 = (String)t2 + "\u2022 <@" + s3 + "> \u2014 `ELO: " + (int)Statistic.ELO.getForPlayer(s3) + "` `" + (int)Statistic.WIN.getForPlayer(s3) + "W/" + (int)Statistic.LOSSES.getForPlayer(s3) + "L` `(" + Statistic.WLR.getForPlayer(s3) + "WLR)`\n";
                }
                BetterEmbed betterEmbed = new BetterEmbed("default", "Game`#" + number + "` QueueStats", "", "", "");
                betterEmbed.addField("Team 1", (String)t1, false);
                betterEmbed.addField("Team 2", (String)t2, false);
                if (game.getState().equals((Object)GameState.STARTING)) {
                    Object remaining = "";
                    for (String s4 : game.getGameMembers()) {
                        remaining = (String)remaining + "\u2022 <@" + s4 + "> \u2014 `" + (int)Statistic.WIN.getForPlayer(s4) + "W/" + (int)Statistic.LOSSES.getForPlayer(s4) + "L` `(" + Statistic.WLR.getForPlayer(s4) + "WLR)`\n";
                    }
                    if (!((String)remaining).equals("")) {
                        betterEmbed.addField("Remaining", (String)remaining, false);
                    }
                }
                betterEmbed.reply(msg);
            } else {
                BetterEmbed.error(Messages.NOT_GAMECHANNEL).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

