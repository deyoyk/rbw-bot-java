/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.scoringsystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class UndoGame
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        block7: {
            if (args2.length == 2) {
                int number = Integer.parseInt(args2[1]);
                Game game = Game.getFromNumber(number);
                try {
                    if (game != null) {
                        if (game.getState().equals((Object)GameState.SCORED)) {
                            c.sendMessageEmbeds(game.voidGame(m3.getId()), new MessageEmbed[0]).queue();
                        } else {
                            BetterEmbed.error(Messages.NOT_SCORED).reply(msg);
                        }
                        break block7;
                    }
                    BetterEmbed.error(Messages.INVALID_GAME).reply(msg);
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
}

