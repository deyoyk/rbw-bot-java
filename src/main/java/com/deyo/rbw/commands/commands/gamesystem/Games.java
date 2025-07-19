/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Games
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        String ID2 = m3.getId();
        if (args2.length > 1) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            ID2 = member.getId();
        }
        int page = 1;
        GameState state = null;
        if (args2.length > 2) {
            try {
                page = Integer.parseInt(args2[2]);
            }
            catch (Exception ex) {
                try {
                    state = GameState.valueOf(args2[2].toUpperCase());
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        if (!Player.isPlayer(ID2)) {
            BetterEmbed reply = new BetterEmbed("error", "Not registered", "", "<@" + ID2 + "> is not registered.", "");
            reply.reply(msg);
            return;
        }
        BetterEmbed reply = Games.showGames(ID2, page, state);
        reply.replyWithButtons(Games.buttons(ID2, state), msg);
    }

    public static List<Button> buttons(String ID2, GameState state) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        String s2 = state == null ? "null" : state.name();
        buttons.add(Button.secondary("previousGAMES-" + ID2 + "-" + s2, "\u2190"));
        buttons.add(Button.secondary("nextGAMES-" + ID2 + "-" + s2, "\u2192"));
        return buttons;
    }

    public static BetterEmbed showGames(String ID2, int page, GameState state) {
        int startingIndex = (page - 1) * 15;
        int endingIndex = page * 15;
        List<Game> games = Game.getPlayerGames(ID2, endingIndex);
        StringBuilder description = new StringBuilder();
        for (int i = startingIndex; i < endingIndex && games.size() > i; ++i) {
            Game game = games.get(i);
            description.append("Game **#").append(game.gamenumber).append("** ");
            if (state != null) {
                if (game.getState() != state) continue;
                if (game.getState() == GameState.SCORED) {
                    if (game.getTeam(game.getWinnerteam()).contains(ID2)) {
                        description.append("\ud83c\udfc6");
                    } else {
                        description.append("\ud83d\udd34");
                    }
                    if (game.getMvps().contains(ID2)) {
                        description.append("\ud83d\udd25");
                    }
                } else if (game.getState() == GameState.VOIDED) {
                    description.append("\u26ab");
                } else {
                    description.append("\u231b");
                }
                description.append("\n");
                continue;
            }
            if (game.getState() == GameState.SCORED) {
                if (game.getTeam(game.getWinnerteam()).contains(ID2)) {
                    description.append("\ud83c\udfc6");
                } else {
                    description.append("\ud83d\udd34");
                }
                if (game.getMvps().contains(ID2)) {
                    description.append("\ud83d\udd25");
                }
            } else if (game.getState() == GameState.VOIDED) {
                description.append("\u26ab");
            } else {
                description.append("\u231b");
            }
            description.append("\n");
        }
        description.append("\n\n\ud83c\udfc6 => Won\n\ud83d\udd34 => Lost\n\ud83d\udd25 => MVP\n\u231b => Pending\n\u26ab => Voided");
        return new BetterEmbed("success", "Games History for " + Player.getName(ID2), "", description.toString(), "Page: " + page);
    }
}

