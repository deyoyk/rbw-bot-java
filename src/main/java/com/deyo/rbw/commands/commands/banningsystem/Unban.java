/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.banningsystem;

import java.util.ArrayList;

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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Unban
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String ID2 = member.getId();
            Object content = "";
            for (String arg : args2) {
                content = (String)content + arg + " ";
            }
            String reason = "Not specified";
            if (args2.length > 2) {
                reason = ((String)content).replaceAll(args2[0], "").replaceAll(args2[1], "").replaceAll(args2[2], "").trim();
            }
            if (Player.isBanned(ID2)) {
                ArrayList<Role> rolestoremove = new ArrayList<Role>();
                rolestoremove.add(RBW.bannedRole);
                g2.modifyMemberRoles((Member)g2.retrieveMemberById(ID2).complete(), null, rolestoremove).queue();
                Player.unban(g2, ID2);
                BetterEmbed embed = new BetterEmbed("success", "Ranked BedWars Moderation", Utils.avatar(g2.getIconUrl()), "You have been unbanned, Feel free to play now!", "");
                embed.addField("User", member.getAsMention(), true);
                embed.addField("Moderator", m3.getAsMention(), true);
                embed.addField("Reason", "`" + reason + "`", true);
                embed.reply(msg);
                if (RBW.banChannel != null) {
                    RBW.banChannel.sendMessage(member.getAsMention()).queue();
                    RBW.banChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
                }
            } else {
                BetterEmbed.error(Messages.NOT_BANNED).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

