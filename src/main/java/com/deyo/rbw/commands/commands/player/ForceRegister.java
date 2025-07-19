/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ForceRegister
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 3) {
            Member target = Utils.getArg(args2[1], g2);
            if (target == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String ign = args2[2];
            if (!new File("RBW/players/" + target.getId() + ".yml").exists()) {
                if (ign.length() <= 16) {
                    if ((ign = ign.replaceAll("[^A-Za-z0-9_\\s]", "")).length() < 3) {
                        BetterEmbed.error(Messages.IGN_TOO_SHORT).reply(msg);
                        return;
                    }
                    Player.createFile(target.getId(), ign);
                    Player.fix(target.getId(), g2);
                    BetterEmbed success = new BetterEmbed("info", "\u2699\uFE0F  Forced Registration", "", "**" + target.getAsMention() + "** is now linked as `" + ign + "`.", "");
                    success.reply(msg);
                } else {
                    BetterEmbed.error(Messages.IGN_TOO_LONG).reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "🚫 That player is already registered. Use `=forcerename` to update their IGN.", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

