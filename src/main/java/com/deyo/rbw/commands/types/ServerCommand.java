/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.types;

import java.io.IOException;

import com.deyo.rbw.childclasses.CommandAdapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public interface ServerCommand {
    public void doCMD(String[] var1, Guild var2, Member var3, MessageChannelUnion var4, CommandAdapter var5, String var6) throws IOException;
}

