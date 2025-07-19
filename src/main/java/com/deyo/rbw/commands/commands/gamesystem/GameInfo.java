/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.io.IOException;
import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GameInfo
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 2) {
            int number = Integer.parseInt(args2[1]);
            Game game = Game.getFromNumber(number);
            if (game != null) {
                GameState gamestate = game.getState();
                BetterEmbed embed = new BetterEmbed("default", "Game`#" + number + "` Info", "", "", "");
                embed.addField("Casual", "" + game.isCasual(), true);
                embed.addField("Channel", "<#" + game.getGameChannel() + ">", true);
                List<String> team1 = game.getTeam(1);
                List<String> team2 = game.getTeam(2);
                Object t1 = "";
                Object t2 = "";
                for (String s2 : team1) {
                    t1 = (String)t1 + "\u2022 <@" + s2 + ">\n";
                }
                for (String s2 : team2) {
                    t2 = (String)t2 + "\u2022 <@" + s2 + ">\n";
                }
                if (game.getMap() != null) {
                    embed.addField("Map", game.getMap(), true);
                }
                embed.setDescription("State: `" + gamestate + "`");
                embed.addField("Team 1", (String)t1, true);
                embed.addField("Team 1", (String)t2, true);
                if (gamestate.equals((Object)GameState.VOIDED)) {
                    embed.addField("Voided by", "<@" + game.getScoredBy() + ">", false);
                } else if (gamestate.equals((Object)GameState.SCORED)) {
                    embed.addField("Scored By", (String)(game.getScoredBy() == null ? "automatic" : "<@" + game.getScoredBy() + ">"), true);
                    Object mvs = "";
                    for (String mvp : game.getMvps()) {
                        mvs = (String)mvs + "\u2022 <@" + mvp + ">\n";
                    }
                    embed.addField("MVP", (String)mvs, true);
                }
                embed.reply(msg);
            } else {
                BetterEmbed.error(Messages.INVALID_GAME).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

