/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.ranksystem;

import java.util.ArrayList;
import java.util.Comparator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.commands.types.ServerCommand;

public class Ranks
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length == 1) {
            BetterEmbed embed;
            ArrayList<String> roles = new ArrayList<String>();
            ArrayList<Rank> formattedRanks = Rank.getRanks();
            formattedRanks.sort(Comparator.comparingInt(Rank::getStartingElo));
            for (Rank rank : formattedRanks) {
                Role role = g2.getRoleById(rank.getId());
                String str = Ranks.getString(rank, role);
                roles.add(str);
            }
            Object ranks = "";
            if (roles.isEmpty()) {
                embed = BetterEmbed.error(Messages.NO_RANKS);
            } else {
                ranks = (String)ranks + "**Registeration:** " + RBW.registeredRole.getAsMention() + "\n";
                ranks = (String)ranks + "**Ranks:**\n\n";
                for (String role : roles) {
                    ranks = (String)ranks + role + "\n";
                }
                embed = new BetterEmbed("normal", Config.getValue("server-name") + " Elo System", "", (String)ranks, "");
            }
            embed.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }

    @NotNull
    private static String getString(Rank rank, Role role) {
        String formattedStartingelo = rank.getStartingElo() < 1 ? "**Under " + rank.getEndingElo() + "**" : (rank.getEndingElo() == 9999 ? "**" + rank.getStartingElo() + "+**" : "**" + rank.getStartingElo() + "**");
        String roleMention = (role != null) ? role.getAsMention() : "`[MISSING ROLE:" + rank.getId() + "]`";
        String str = formattedStartingelo + " " + roleMention + "  W:(+" + rank.getWinElo() + ") L:(-" + rank.getLoseElo() + ")  MVP: (+" + rank.getMvpElo() + ") BED: (+" + rank.getBedElo() + ")";
        if (rank.getDecay() != -1) {
            str = str + " (Decay: -" + rank.getDecay() + ")";
        }
        return str;
    }
}

