/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.banningsystem;

import java.io.IOException;
import java.util.HashMap;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.commands.banningsystem.Ban;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

public class Strike
implements ServerCommand {
    public static HashMap<String, String> pendingStrikes = new HashMap<String, String>();
    
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 3) {
            // Build confirmation embed first
            Member targetMember = Utils.getArg(args2[1], g2);
            if (targetMember == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String reasonPreview = String.join(" ", java.util.Arrays.copyOfRange(args2, 2, args2.length));
            String strikeKey = targetMember.getId() + ":" + m3.getId();
            pendingStrikes.put(strikeKey, reasonPreview);
            BetterEmbed confirm = BetterEmbed.warning("Confirm Strike", "Are you sure you want to issue a strike to **" + targetMember.getEffectiveName() + "** for: *" + reasonPreview + "* ?");
            var confirmBtn = net.dv8tion.jda.api.interactions.components.buttons.Button.success("confirmStrike:" + targetMember.getId() + ":" + m3.getId(), "Yes");
            var cancelBtn  = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("cancelStrike:" + targetMember.getId() + ":" + m3.getId(), "No");
            confirm.replyWithButtons(java.util.List.of(confirmBtn, cancelBtn), msg);
            return;
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }

    public static void strike(String ID2, Guild g2, Member striker, Member striked, String reason, CommandAdapter msg) throws IOException {
        Player.lose(ID2, g2, 0, null);
        int oldStrikes = (int)Statistic.STRIKES.getForPlayer(ID2);
        int newStrikes = oldStrikes + 1;
        Statistic.STRIKES.setForPlayer(ID2, newStrikes);
        int duration = Integer.parseInt(Config.getValue("strike" + newStrikes));
        BetterEmbed embed = new BetterEmbed("default", "Ranked BedWars Moderation", Utils.avatar(g2.getIconUrl()), "You have been striked for breaking server rules", "");
        embed.addField("User", striked.getAsMention(), true);
        embed.addField("Moderator", striker.getAsMention(), true);
        embed.addField("Reason", "`" + reason + "`", true);
        embed.addField("Strikes", "`" + oldStrikes + " -> " + newStrikes + "`", true);
        if (duration != 0) {
            if (!Player.isBanned(ID2)) {
                BetterEmbed banEmbed = Ban.ban(g2, striked.getId(), duration + "h", reason, striked, striker);
                if (RBW.banChannel != null) {
                    ((MessageCreateAction)RBW.banChannel.sendMessage(striked.getAsMention()).setEmbeds(banEmbed.build())).queue();
                }
                embed.addField("<@" + ID2 + "> is now banned for: ", "`" + duration + " hours`", false);
            } else {
                embed.addField("WARNING", "I couldn't ban the player since they're already banned", false);
            }
        }
        embed.reply(msg);
        if (RBW.banChannel != null) {
            ((MessageCreateAction)RBW.banChannel.sendMessage(striked.getAsMention()).setEmbeds(embed.build())).queue();
        }
    }
}

