/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.banningsystem;

import java.io.File;
import java.io.FileNotFoundException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class BanInfo
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws FileNotFoundException {
        if (args2.length == 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            String ID2 = member.getId();
            if (Player.isBanned(ID2)) {
                YamlConfiguration banData = YamlConfiguration.loadConfiguration(new File("RBW/bans/" + ID2 + ".yml"));
                String banned = banData.get("banned").toString();
                long unbanned = banData.getLong("unbanned");
                String reason = banData.get("reason").toString();
                Object text = "";
                try {
                    text = "<@!" + ID2 + ">\nBanned On `" + banned + " GMT`\nUnbanned On <t:" + unbanned / 1000L + ":F> \n**Reason: **" + reason;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                BetterEmbed embed = new BetterEmbed("default", "Ban info for `" + Player.getName(ID2) + "`", "", (String)text, "");
                embed.reply(msg);
            } else {
                BetterEmbed.error(Messages.NOT_BANNED).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

