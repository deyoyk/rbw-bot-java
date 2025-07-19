/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.ingame.Main;
import com.deyo.rbw.ingame.PINHandling;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Rename
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            String ign = args2[1];
            if (ign.length() <= 16) {
                try {
                    ign = ign.replaceAll("[^A-Za-z0-9_\\s]", "");
                    if (ign.length() < 3) {
                        BetterEmbed.error(Messages.IGN_TOO_SHORT).reply(msg);
                        return;
                    }
                    int pin = PINHandling.generatePin(m3.getId(), ign);
                    BetterEmbed embed = new BetterEmbed("info", "\u270F\uFE0F  Update Your IGN", "", "Follow the steps below to update your Minecraft IGN:\n\u0031\uFE0F\u20E3  Join **" + Main.serverIp + "**\n\u0032\uFE0F\u20E3  Run `/link " + pin + "` in chat\n\n\u23F3 This code expires in **5 minutes**.\nRun `=rename <IGN>` again to generate a new code.", "");
                    embed.reply(msg);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                BetterEmbed.error(Messages.IGN_TOO_LONG).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

