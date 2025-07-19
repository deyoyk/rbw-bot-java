/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.screensharingsystem;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.screenshare.ScreenShare;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.utils.FileUpload;

public class Unfreeze
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length == 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            Role freezeRole = RBW.frozenRole;
            if (freezeRole == null) {
                BetterEmbed error = new BetterEmbed("error", "Critical Error", "", "Report this to the owner (FREEZE_ROLE_NOT_SET)", "");
                error.reply(msg);
                return;
            }
            if (!member.getRoles().contains(freezeRole)) {
                BetterEmbed error = new BetterEmbed("error", "", "", "That player isn't frozen (Doesn't have " + freezeRole.getAsMention() + " role)", "");
                error.reply(msg);
                return;
            }
            BetterEmbed error1 = new BetterEmbed("success", "Done", "", "Unfroze the member " + member.getAsMention() + "\n :wastebasket: Transcript saving, This channel will be deleted in 10 seconds", "");
            error1.reply(msg);
            g2.removeRoleFromMember(member, freezeRole).queue();
            ScreenShare ss = ScreenShare.getByScreenShared(member.getId());
            if (ss != null) {
                String channelid = ss.getChannelID();
                TextChannel channel = g2.getTextChannelById(channelid);
                if (channel != null) {
                    channel.delete().queueAfter(10L, TimeUnit.SECONDS);
                }
                BetterEmbed embed1 = new BetterEmbed("default", "SS Closed", "", "Reason: **Closed by **" + m3.getAsMention() + "\nTarget: " + member.getAsMention(), "");
                TextChannel logs = RBW.ssLogsChannel;
                logs.sendMessageEmbeds(embed1.build(), new MessageEmbed[0]).queue();
                Transcript transcript = Transcript.getTranscript(c.getId());
                if (transcript == null) {
                    return;
                }
                File f = transcript.toFile();
                if (f == null) {
                    return;
                }
                logs.sendFiles(FileUpload.fromData(f)).queue();
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

