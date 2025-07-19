/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PrefixToggle
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        String s2;
        Role togglePrefix = RBW.prefixToggleRole;
        if (togglePrefix == null) {
            BetterEmbed reply = new BetterEmbed("error", "", "", "⚠️ Prefix-toggle role not configured. Please contact an admin.", "");
            reply.reply(msg);
            return;
        }
        boolean on = m3.getRoles().contains(togglePrefix);
        if (args2.length > 1) {
            on = args2[1].equalsIgnoreCase("off");
        }
        if (on) {
            g2.removeRoleFromMember(m3, togglePrefix).queue(r -> Player.fix(m3.getId(), g2));
            s2 = "Off";
        } else {
            g2.addRoleToMember(m3, togglePrefix).queue(r -> Player.fix(m3.getId(), g2));
            s2 = "On";
        }
        BetterEmbed reply = new BetterEmbed("info", "\uD83D\uDCE3  Prefix Toggle", "", "Prefix visibility is now **" + s2 + "**.", "");
        reply.reply(msg);
    }
}

