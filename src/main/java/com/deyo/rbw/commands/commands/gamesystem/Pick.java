/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Pick
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (!Game.isGameChannel(c.getId())) {
            BetterEmbed.error(Messages.NOT_GAMECHANNEL).reply(msg);
            return;
        }
        int gamenumber = Game.getNumber(c.getId());
        Game game = Game.getFromNumber(gamenumber);
        if (!game.getState().equals((Object)GameState.STARTING)) {
            BetterEmbed.error(Messages.GAME_ALREADY_STARTED).reply(msg);
            return;
        }
        if (!game.getCaptain(1).equals(m3.getId()) && !game.getCaptain(2).equals(m3.getId())) {
            BetterEmbed.error(Messages.NOT_CAPTAIN).reply(msg);
            return;
        }
        if (!game.getCurrentCaptain().equals(m3.getId())) {
            BetterEmbed error = new BetterEmbed("error", "", "", "It's <@!" + game.getCurrentCaptain() + ">'s turn to pick", "");
            error.reply(msg);
            return;
        }
        if (args2.length == 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String ID2 = member.getId();
            if (member == m3) {
                BetterEmbed.error(Messages.PICKED_YOURSELF).reply(msg);
                return;
            }
            byte b = game.pick(m3, member, g2);
            if (b == 0) {
                BetterEmbed error = new BetterEmbed("error", "", "", member.getAsMention() + " is not in remaining players", "");
                error.reply(msg);
                return;
            }
            if (b == 2) {
                c.sendMessage("Couldn't assign a map for this game...Wait until some games are done").queue();
                return;
            }
            if (b == 1) {
                return;
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

