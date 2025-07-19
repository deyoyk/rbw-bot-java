/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.banningsystem;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Ban
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 4) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            if (Player.isBanned(member.getId())) {
                BetterEmbed.error(Messages.ALREADY_BANNED).reply(msg);
                return;
            }
            String ID2 = member.getId();
            String duration = args2[2];
            Object content = "";
            for (String arg : args2) {
                content = (String)content + arg + " ";
            }
            String reason = ((String)content).replaceAll(args2[0], "").replaceAll(args2[1], "").replaceAll(args2[2], "").trim();
            if (duration.contains("m") || duration.contains("h") || duration.contains("d") || duration.contains("s")) {
                Ban.ban(g2, ID2, duration, reason, member, m3).reply(msg);
            } else {
                BetterEmbed.error(Messages.INVALID_TIME_FORMAT).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }

    public static BetterEmbed ban(Guild g2, String ID2, String duration, String reason, Member bannedGuy, Member moderator) {
        long expiring = Player.ban(g2, ID2, duration, reason);
        String expires = "<t:" + expiring / 1000L + ">";
        String expiresRel = "<t:" + expiring / 1000L + ":R>";
        BetterEmbed embed = new BetterEmbed("normal", "Ranked BedWars Moderation", Utils.avatar(g2.getIconUrl()), "You have been banned for breaking the server rules", "");
        embed.addField("User", bannedGuy.getAsMention(), true);
        embed.addField("Moderator", moderator.getAsMention(), true);
        embed.addField("Duration", duration, true);
        embed.addField("Expiry", expires + " (" + expiresRel + ")", true);
        embed.addField("Reason", "`" + reason + "`", true);
        if (RBW.banChannel != null) {
            RBW.banChannel.sendMessage(bannedGuy.getAsMention()).queue();
            RBW.banChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
        }
        return embed;
    }
}

