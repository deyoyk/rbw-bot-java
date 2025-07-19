/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Msg;
import com.deyo.rbw.childclasses.Perms;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ReloadConfig
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        BetterEmbed.reply("Reloading..", msg);
        RBW.load(g2.getJDA());
        Msg.reloadMsg();
        Perms.reloadPerms();
        Config.reloadConfig();
        for (Themes theme : Themes.list()) {
            theme.reloadConfig();
        }
        BetterEmbed.reply("Reloaded successfully.", msg);
    }
}

