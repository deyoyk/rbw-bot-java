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
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Bans
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws FileNotFoundException {
        if (args2.length == 1) {
            if (new File("RBW/bans").listFiles().length == 0) {
                BetterEmbed error = new BetterEmbed("error", "", "", "There are no bans!", "");
                error.reply(msg);
                return;
            }
            Object text = "";
            for (File f : new File("RBW/bans").listFiles()) {
                try {
                    YamlConfiguration banData = YamlConfiguration.loadConfiguration(f);
                    String ID2 = f.getName().replaceAll(".yml", "");
                    String banned = banData.get("banned").toString();
                    long unbanned = banData.getLong("unbanned");
                    String reason = banData.get("reason").toString();
                    text = (String)text + "<@!" + ID2 + ">\nBanned On `" + banned + " GMT`\nUnbanned On <t:" + unbanned / 1000L + ":F> \n**Reason: **" + reason + "\n\n";
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            BetterEmbed embed = new BetterEmbed("default", "All Bans `(" + new File("RBW/bans").listFiles().length + ")`", "", (String)text, "");
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

