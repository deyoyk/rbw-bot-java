/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.ranksystem;

import java.io.IOException;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ModifyRank
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length >= 4) {
            BetterEmbed error;
            String amount = args2[3].replaceAll("-", "");
            String ID2 = args2[1].replaceAll("[^0-9]", "");
            Rank rank = Rank.getRankFromID(ID2);
            if (!Utils.isInteger(amount)) {
                error = new BetterEmbed("error", "Invalid number", "", "Please specify a valid number for the amount.", "");
                error.reply(msg);
            }
            if (rank == null) {
                error = new BetterEmbed("error", "Invalid rank", "", "The rank you specified does not exist.", "");
                error.reply(msg);
                return;
            }
            String type = args2[2];
            int amountN = Integer.parseInt(amount);
            switch (type) {
                case "mvpelo": 
                case "mvp": {
                    rank.setMvpElo(amountN);
                    break;
                }
                case "winelo": 
                case "win": {
                    rank.setWinElo(amountN);
                    break;
                }
                case "loseelo": 
                case "lose": {
                    rank.setLoseElo(amountN);
                    break;
                }
                case "bed": 
                case "bedelo": {
                    rank.setBedElo(amountN);
                    break;
                }
                case "decayelo": 
                case "decay": {
                    rank.setDecay(amountN);
                    break;
                }
                case "startingelo": {
                    rank.setStartingElo(amountN);
                    break;
                }
                case "endingelo": {
                    rank.setEndingElo(amountN);
                    break;
                }
                default: {
                    BetterEmbed error2 = new BetterEmbed("error", "Invalid type", "", "Valid types: **mvp, win, lose, decay, bed, startingelo, endingelo**.", "");
                    error2.reply(msg);
                    return;
                }
            }
            BetterEmbed success = new BetterEmbed("success", "Successfully edited rank", "", "The rank " + args2[1] + "'s " + type + " has been set to " + amount, "");
            success.reply(msg);
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

