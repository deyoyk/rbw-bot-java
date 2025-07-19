/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.scoringsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class Submit
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 1) {
            if (Game.isGameChannel(c.getId())) {
                List<Message.Attachment> attachments = BetterEmbed.getAttachments(msg);
                if (attachments.size() == Integer.parseInt(Config.getValue("submitting-attachments"))) {
                    int number = Game.getNumber(c.getId());
                    Game game = Game.getFromNumber(number);
                    if (game.getState() != GameState.PLAYING) {
                        BetterEmbed error = new BetterEmbed("error", "", "", "You can't submit the game at this state.", "");
                        c.sendMessageEmbeds(error.build(), new MessageEmbed[0]).queue();
                        return;
                    }
                    if (!game.isCasual()) {
                        BetterEmbed embed = new BetterEmbed("default", "Game Submitted", "", "Waiting for scorer to score the game.", "");
                        Object queues = "";
                        for (Queue queue : Queue.getQueues()) {
                            queues = (String)queues + "<#" + queue.getId() + ">\n";
                        }
                        embed.addField("Requeue here:", (String)queues, false);
                        c.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
                        try {
                            ArrayList<PermissionOverride> overrides = new ArrayList<PermissionOverride>(c.asTextChannel().getMemberPermissionOverrides());
                            for (PermissionOverride po : overrides) {
                                po.delete().queue();
                            }
                            Objects.requireNonNull(g2.getVoiceChannelById(game.getVoiceChannel(1))).delete().queue();
                            Objects.requireNonNull(g2.getVoiceChannelById(game.getVoiceChannel(2))).delete().queue();
                            String string = game.getMap();
                            BetterEmbed scoring = new BetterEmbed("default", "Game`#" + number + "` Scoring", "", "", "");
                            ArrayList<String> team1 = new ArrayList<String>(game.getTeam(1));
                            ArrayList<String> team2 = new ArrayList<String>(game.getTeam(2));
                            Object t1 = "";
                            Object t2 = "";
                            for (String s3 : team1) {
                                t1 = (String)t1 + "\u2022 <@" + s3 + ">\n";
                            }
                            for (String s2 : team2) {
                                t2 = (String)t2 + "\u2022 <@" + s2 + ">\n";
                            }
                            scoring.addField("Team 1:", (String)t1, false);
                            scoring.addField("Team 2:", (String)t2, false);
                            String a = attachments.get(0).getUrl();
                            long minMillis = System.currentTimeMillis() - 300000L;
                            ArrayList<MessageEmbed> ausiliaryEmbeds = new ArrayList<MessageEmbed>();
                            Transcript t = Transcript.getTranscript(c.getId());
                            if (t != null) {
                                for (Map.Entry<Long, String> entry : t.getAttachments().entrySet()) {
                                    String attachment;
                                    long millis = entry.getKey();
                                    if (millis <= minMillis || (attachment = entry.getValue()).equals(a)) continue;
                                    BetterEmbed ausiliaryEmbed = new BetterEmbed("default", "Possible Bed SS", "", "", "");
                                    ausiliaryEmbed.setImage(attachment);
                                    if (ausiliaryEmbeds.size() >= 3) continue;
                                    ausiliaryEmbeds.add(ausiliaryEmbed.build());
                                }
                            }
                            scoring.setImage(a);
                            scoring.setDescription("Map: `" + string + "`");
                            game.setState(GameState.SUBMITTED);
                            ausiliaryEmbeds.add(scoring.build());
                            ((MessageCreateAction)c.sendMessage("<@&" + Config.getValue("scorer-role") + ">").setEmbeds(ausiliaryEmbeds)).queue();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        BetterEmbed.error(Messages.CASUAL_GAME).reply(msg);
                    }
                } else {
                    BetterEmbed error = new BetterEmbed("error", "", "", "You need to attach " + Config.getValue("submitting-attachments") + " screenshot(s) for proof", "");
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

