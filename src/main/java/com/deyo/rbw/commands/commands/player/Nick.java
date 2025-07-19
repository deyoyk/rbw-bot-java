/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Nick
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        String ID2 = m3.getId();
        if (args2.length == 1) {
            Player.setNick(ID2, "");
            BetterEmbed info = new BetterEmbed("info", "\uD83D\uDD90\uFE0F  Nickname Reset", "", "Your nickname has been cleared.", "");
            info.reply(msg);
            Player.fix(ID2, g2);
            return;
        }
        Object nick = "";
        for (int i = 1; i < args2.length; ++i) {
            nick = i == 1 ? (String)nick + args2[i] : (String)nick + " " + args2[i];
        }
        if (((String)nick).length() > 16) {
            BetterEmbed reply = new BetterEmbed("error", "", "", "🚫 Nickname can’t exceed **16** characters.", "");
            reply.reply(msg);
            return;
        }
        Player.setNick(ID2, (String)nick);
        BetterEmbed reply = new BetterEmbed("info", "\uD83D\uDD90\uFE0F  Nickname Set", "", "Your nickname is now `" + (String)nick + "`.", "");
        reply.reply(msg);
        Player.fix(ID2, g2);
    }
}

