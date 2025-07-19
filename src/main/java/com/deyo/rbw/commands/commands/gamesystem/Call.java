/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.gamesystem;

import java.io.IOException;
import java.util.Objects;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Call
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length >= 2) {
            AudioChannelUnion VC = Objects.requireNonNull(m3.getVoiceState()).getChannel();
            if (VC == null) {
                BetterEmbed error = new BetterEmbed("error", "", "", "You are not in a voice channel.", "");
                error.reply(msg);
                return;
            }
            for (int i = 1; i < args2.length; ++i) {
                Member member = Utils.getArg(args2[1], g2);
                if (member == null) {
                    BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                    return;
                }
                String playerVC = VC.getId();
                Game game = Game.getFromVC(playerVC);
                if (game == null) {
                    BetterEmbed error = new BetterEmbed("error", "", "", "You are not in a game's voice channel.", "");
                    error.reply(msg);
                    return;
                }
                BetterEmbed success = new BetterEmbed("success", "", "", member.getAsMention() + " has now permission to join " + VC.getAsMention(), "");
                success.reply(msg, member.getAsMention());
                if (VC.getUserLimit() != 0) {
                    VC.getManager().setUserLimit(VC.getUserLimit() + 1).queue();
                }
                VC.upsertPermissionOverride(member).setAllowed(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT).queue();
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

