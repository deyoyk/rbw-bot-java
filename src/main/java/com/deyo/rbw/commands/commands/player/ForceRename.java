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

public class ForceRename
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
            if (new File("RBW/players/" + target.getId() + ".yml").exists()) {
                if (ign.length() <= 16) {
                    try {
                        ign = ign.replaceAll("[^A-Za-z0-9_\\s]", "");
                        if (ign.length() < 3) {
                            BetterEmbed.error(Messages.IGN_TOO_SHORT).reply(msg);
                            return;
                        }
                        String oldname = Player.getName(target.getId());
                        Player.setName(target.getId(), ign);
                        BetterEmbed embed = new BetterEmbed("info", "\u270F\uFE0F  Forced Rename", "", "**" + target.getAsMention() + "**: `" + oldname + "` ➜ `" + ign + "`", "");
                        embed.reply(msg);
                        Player.fix(target.getId(), g2);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    BetterEmbed.error(Messages.IGN_TOO_LONG).reply(msg);
                }
            } else {
                BetterEmbed error = new BetterEmbed("error", "", "", "🚫 That player is not registered. Use `=forceregister` first.", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

