/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.CommandManager;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class RemoveNick
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        Member a;
        String ID2 = m3.getId();
        if (CommandManager.checkPerms("staff", m3, g2) && args2.length > 1 && (a = Utils.getArg(args2[1], g2)) != null) {
            ID2 = a.getId();
        }
        Player.setNick(ID2, "");
        BetterEmbed info = new BetterEmbed("info", "\uD83D\uDD90\uFE0F  Nickname Cleared", "", "Nickname for **" + Player.getName(ID2) + "** has been reset.", "");
        info.reply(msg);
        Player.fix(ID2, g2);
    }
}

