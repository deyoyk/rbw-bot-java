/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.ingame.Main;
import com.deyo.rbw.ingame.PINHandling;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Register
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            String ign = args2[1];
            if (ign.length() <= 16) {
                String ID2 = m3.getId();
                File f = new File("RBW/players/" + ID2 + ".yml");
                if (f.exists()) {
                    BetterEmbed error = new BetterEmbed("error", "", "", "You are already registered", "");
                    error.reply(msg);
                    Player.fix(ID2, g2);
                    return;
                }
                try {
                    ign = ign.replaceAll("[^A-Za-z0-9_\\s]", "");
                    if (ign.length() < 3) {
                        BetterEmbed.error(Messages.IGN_TOO_SHORT).reply(msg);
                        return;
                    }
                    int pin = PINHandling.generatePin(ID2, ign);
                    BetterEmbed embed = new BetterEmbed("info", "\uD83D\uDD17  Link Your Account", "", "Follow the steps below to link your Minecraft IGN:\n\u0031\uFE0F\u20E3  Join **" + Main.serverIp + "**\n\u0032\uFE0F\u20E3  Run `/link " + pin + "` in chat\n\n\u23F3 This code expires in **5 minutes**.\nRun `=register <IGN>` again to generate a new code.", "");
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
