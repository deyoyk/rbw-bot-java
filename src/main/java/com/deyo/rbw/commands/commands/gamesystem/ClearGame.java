/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ClearGame
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, final Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length >= 1) {
            int number;
            Game game = null;
            if (Game.isGameChannel(c.getId())) {
                number = Game.getNumber(c.getId());
                game = Game.getFromNumber(number);
            }
            if (args2.length > 1) {
                number = -1;
                try {
                    number = Integer.parseInt(args2[1]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                game = Game.getFromNumber(number);
            }
            if (game != null) {
                Object content = "";
                for (String arg : args2) {
                    content = (String)content + arg + " ";
                }
                BetterEmbed embed1 = new BetterEmbed("default", "Command sent", "", "**" + (String)content + "**\nBy " + m3.getAsMention(), "");
                if (RBW.scoringLogsChannel != null) {
                    RBW.scoringLogsChannel.sendMessageEmbeds(embed1.build(), new MessageEmbed[0]).queue();
                }
                BetterEmbed.reply("The game channel will get deleted in 15 Seconds...", msg);
                if (game.getState() != GameState.SCORED) {
                    game.setState(GameState.VOIDED);
                    game.setScoredBy(m3.getId());
                }
                game.kickPlayersFromGame();
                final Game finalGame = game;
                TimerTask task = new TimerTask(){

                    @Override
                    public void run() {
                        finalGame.deleteChats(g2, true);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 15000L);
            } else {
                BetterEmbed.error(Messages.NOT_GAMECHANNEL).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

