/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.ranksystem;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class DeleteRank
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 2) {
            String ID2 = args2[1].replaceAll("[^0-9]", "");
            if (Rank.getRankFromID(ID2) != null) {
                Rank.delRank(ID2);
                BetterEmbed success = new BetterEmbed("success", "", "", Messages.RANK_DELETED.get(), "");
                success.reply(msg);
            } else {
                BetterEmbed.error(Messages.INVALID_RANK).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

