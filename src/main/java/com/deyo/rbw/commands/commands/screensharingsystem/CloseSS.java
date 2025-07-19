/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.screensharingsystem;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.screenshare.ScreenShare;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.utils.FileUpload;

public class CloseSS
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        ScreenShare ss = null;
        for (ScreenShare screenShare : ScreenShare.screenShares) {
            if (!screenShare.getChannelID().equalsIgnoreCase(c.getId())) continue;
            ss = screenShare;
            break;
        }
        if (ss == null) {
            BetterEmbed error = new BetterEmbed("error", "Error", "", "This channel is not an SS", "");
            error.reply(msg);
            return;
        }
        BetterEmbed error1 = new BetterEmbed("success", "Done", "", ":wastebasket: Transcript saving, This channel will be deleted in 10 seconds", "");
        error1.reply(msg);
        ss.delete();
        c.delete().queueAfter(10L, TimeUnit.SECONDS);
        BetterEmbed embed1 = new BetterEmbed("default", "SS Closed", "", "Reason: **Closed by **" + m3.getAsMention() + "\nTarget: <@" + ss.getScreenShared() + ">", "");
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
}

