/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.util.Timer;
import java.util.TimerTask;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class Void
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, final MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 1) {
            if (Game.isGameChannel(c.getId())) {
                int number = Game.getNumber(c.getId());
                Game game = Game.getFromNumber(number);
                GameState state = game.getState();
                if (state.equals((Object)GameState.STARTING) || state.equals((Object)GameState.PLAYING)) {
                    BetterEmbed done = new BetterEmbed("default", "Game`#" + number + "` Voiding", "", "If this command was abused, please ss this and make a report ticket\n\nGame channel closing after `30s`", "");
                    BetterEmbed embed = new BetterEmbed("default", "Game`#" + number + "` Voiding", "", "Votes will be counted in `30s`", "");
                    embed.addField("Requested by: ", m3.getAsMention(), false);
                    StringBuilder mentions = new StringBuilder();
                    for (String gameMember : game.getGameMembers()) {
                        mentions.append("<@").append(gameMember).append(">");
                    }
                    for (String gameMember : game.getTeam(1)) {
                        mentions.append("<@").append(gameMember).append(">");
                    }
                    for (String gameMember : game.getTeam(2)) {
                        mentions.append("<@").append(gameMember).append(">");
                    }
                    c.sendMessage(mentions.toString()).queue();
                    c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue(message -> {
                        message.addReaction(Emoji.fromUnicode("\u2705")).queue();
                        message.addReaction(Emoji.fromUnicode("\u274c")).queue();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message1 = c.retrieveMessageById(message.getId()).complete();
                                if (message1.getReactions().get(0).getCount() - 2 >= message1.getReactions().get(1).getCount()) {
                                    done.addField("Requested by: ", m3.getAsMention(), false);
                                    if (game.getState() != GameState.SCORED) {
                                        game.setState(GameState.VOIDED);
                                    }
                                    game.kickPlayersFromGame();
                                    game.setScoredBy(m3.getId());
                                    game.deleteChats(g2, true);
                                } else {
                                    done.setDescription("Voiding Cancelled");
                                }
                                message1.editMessageEmbeds(done.build()).queue();
                                message1.clearReactions().queue();
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 30000L);
                    });
                } else {
                    BetterEmbed error = new BetterEmbed("error", "", "", "You can't void a game at this state.", "");
                    error.reply(msg);
                }
            } else {
                BetterEmbed.error(Messages.NOT_GAMECHANNEL).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

