/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.banningsystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class UnStrike
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length >= 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String ID2 = member.getId();
            Player.unlose(ID2, g2);
            int strikesnow = (int)Statistic.STRIKES.getForPlayer(ID2);
            Statistic.STRIKES.setForPlayer(ID2, Statistic.STRIKES.getForPlayer(ID2) - 1.0);
            BetterEmbed embed = new BetterEmbed("default", "Ranked BedWars Moderation", Utils.avatar(g2.getIconUrl()), "You have been unstriked!", "");
            embed.addField("User", member.getAsMention(), true);
            embed.addField("Moderator", m3.getAsMention(), true);
            embed.addField("Strikes", "`" + strikesnow + " -> " + (strikesnow - 1) + "`", true);
            embed.reply(msg);
            if (RBW.banChannel != null) {
                ((MessageCreateAction)RBW.banChannel.sendMessage(member.getAsMention()).setEmbeds(embed.build())).queue();
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

