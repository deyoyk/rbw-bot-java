/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.events.QueueEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class WipeGames
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        for (Game game : Game.games) {
            game.getStoring().getF().delete();
        }
        Game.games.clear();
        QueueEvent.serverData.set("games-played", 0);
        BetterEmbed success = new BetterEmbed("success", "Reset all games", "", "All games have been reset!", "");
        success.reply(msg);
    }
}

