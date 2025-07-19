/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.banningsystem;

import java.io.IOException;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.commands.banningsystem.Strike;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class StrikeRequest
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length > 2) {
            Member target = Utils.getArg(args2[1], g2);
            if (target == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            Object reason = "";
            for (int i = 0; i < args2.length; ++i) {
                if (i <= 1) continue;
                reason = (String)reason + args2[i];
            }
            BetterEmbed request = new BetterEmbed("default", "Strike request", Utils.avatar(g2.getIconUrl()), "It needs atleast **3 positive votes** to strike this player", "");
            request.addField("User", target.getAsMention(), true);
            request.addField("Requested by", m3.getAsMention(), true);
            request.addField("Reason", (String)reason, true);
            c.sendMessageEmbeds(request.build(), new MessageEmbed[0]).queue(message -> {
                message.addReaction(Emoji.fromUnicode("\u2705")).queue();
                message.addReaction(Emoji.fromUnicode("\u274c")).queue();
                Main.runTaskLater(() -> {
                    int no;
                    int yes = message.getReactions().get(0).getCount() - 1;
                    if (yes - (no = message.getReactions().get(1).getCount() - 1) >= Config.getConfig().getInt("min-vouches")) {
                        try {
                            Strike.strike(target.getId(), g2, g2.getSelfMember(), m3, "Reached " + (yes - no) + " vouches (" + message.getId() + ")", msg);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 360000L);
            });
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

