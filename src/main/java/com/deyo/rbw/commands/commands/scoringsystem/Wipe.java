/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.scoringsystem;

import java.util.Set;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Wipe
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            if (args2[1].equals("everyone")) {
                Set<String> players = Player.getPlayers().keySet();
                float elapsedTime = (float)(2000 * players.size()) / 1000.0f;
                BetterEmbed embed = new BetterEmbed("default", "Resetting everyone's stats...", "", "`Check console for more details`", "");
                embed.addField("WARNING", "please do not use any other cmd during the reset\nit might result into errors / slower resetting", false);
                embed.addField("Estimated time", elapsedTime + " second(s) `(" + players.size() + " players)`", false);
                embed.addField("Reset by:", m3.getAsMention(), true);
                embed.reply(msg);
                for (String ID2 : players) {
                    Player.wipe(ID2);
                    Player.fix(ID2, g2);
                }
                BetterEmbed success = new BetterEmbed("success", "All stats were successfully reset", "", "", "");
                success.addField("Resetting will take", "`" + elapsedTime + "` seconds `(" + players.size() + " players)`", true);
                success.addField("Reset by:", m3.getAsMention(), true);
                success.reply(msg);
            } else {
                Member member = Utils.getArg(args2[1], g2);
                if (member == null) {
                    BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                    return;
                }
                Player.wipe(member.getId());
                Player.fix(member.getId(), g2);
                BetterEmbed embed = new BetterEmbed("success", "Stats wiped", "", Messages.SUCCESS_WIPED.get(), "");
                embed.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

