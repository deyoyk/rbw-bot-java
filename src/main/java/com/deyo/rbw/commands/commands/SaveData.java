/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands;

import java.io.IOException;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class SaveData
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        BetterEmbed.reply("saving the player, ranks etc data...", msg);
        Main.saveData();
        BetterEmbed.reply("Data successfully saved", msg);
    }
}

