/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.ranksystem;

import java.io.File;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class AddRank
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 8) {
            Role role = null;
            String ID2 = args2[1].replaceAll("[^0-9]", "");
            try {
                role = g2.getRoleById(ID2);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (role != null) {
                for (int i = 2; i < 9 && args2.length != i; ++i) {
                    if (Utils.isInteger(args2[i])) continue;
                    BetterEmbed error = new BetterEmbed("error", "Not a number", "", "One of the numbers you specified is not valid. Please make sure you are configurating it correctly.", "");
                    error.reply(msg);
                    return;
                }
                String startingElo = args2[2];
                String endingElo = args2[3];
                String winElo = args2[4];
                String loseElo = args2[5];
                String mvpElo = args2[6];
                String bedElo = args2[7];
                String decay = "";
                if (args2.length > 8) {
                    decay = args2[8];
                }
                if (!new File("RBW/ranks/" + ID2 + ".yml").exists()) {
                    Rank r = Rank.addRank(ID2, startingElo, endingElo, winElo, loseElo, mvpElo, bedElo);
                    BetterEmbed success = new BetterEmbed("success", "\u2705 successfully added `" + role.getName() + "` rank", "", "", "");
                    success.addField("Role:", role.getAsMention(), true);
                    success.addField("Starting elo:", startingElo, true);
                    success.addField("Ending elo:", endingElo, true);
                    success.addField("Win elo:", "+" + winElo, true);
                    success.addField("Lose elo:", "-" + loseElo, true);
                    success.addField("Mvp Elo:", mvpElo, true);
                    success.addField("Bed Elo:", bedElo, true);
                    if (!decay.isEmpty()) {
                        decay = decay.replaceAll("-", "");
                        r.setDecay(Integer.parseInt(decay));
                        success.addField("Decay Elo:", "-" + decay, true);
                    }
                    success.reply(msg);
                } else {
                    BetterEmbed.error(Messages.INVALID_ROLE).reply(msg);
                }
            } else {
                BetterEmbed.error(Messages.INVALID_ROLE).reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

