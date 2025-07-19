/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.scoringsystem;

import java.io.IOException;
import java.util.ArrayList;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Score
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        block8: {
            if (args2.length >= 4) {
                int number = Integer.parseInt(args2[1]);
                int teamwinner = Integer.parseInt(args2[2]);
                ArrayList<String> mvps = new ArrayList<String>();
                if (!args2[3].equalsIgnoreCase("none")) {
                    for (int i = 3; i < 10 && args2.length > i; ++i) {
                        String ID2 = args2[i];
                        Member member = Utils.getArg(ID2, g2);
                        if (member == null) {
                            BetterEmbed error = new BetterEmbed("error", "", "", "Wrong Target! Please specify a valid member.", "");
                            error.reply(msg);
                            return;
                        }
                        mvps.add(member.getId());
                    }
                }
                Game game = Game.getFromNumber(number);
                try {
                    if (game != null && (teamwinner == 1 || teamwinner == 2)) {
                        BetterEmbed embed = game.score(m3.getId(), teamwinner, mvps);
                        embed.reply(msg);
                        break block8;
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

