/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.admin;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.utils.FileUpload;

public class GetPlayerFile
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            Member target = Utils.getArg(args2[1], g2);
            if (target != null) {
                BetterEmbed success = new BetterEmbed("success", "", "", "Here's the player file", "");
                success.reply(msg);
                c.sendFiles(FileUpload.fromData(new File("RBW/players/" + target.getId() + ".yml"))).queue();
            } else {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

