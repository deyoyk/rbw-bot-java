/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.screensharingsystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.classes.screenshare.ScreenShare;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;

public class SS
implements ServerCommand {
    private static List<Button> buttons(String target) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(Button.secondary("Accept-SS::" + target, "\u2705"));
        buttons.add(Button.secondary("Deny-SS::" + target, "\u274c"));
        return buttons;
    }

    @Override
    public void doCMD(String[] args2, final Guild g2, final Member m3, final MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (args2.length >= 3) {
            List<Message.Attachment> attachments = BetterEmbed.getAttachments(msg);
            if (attachments.size() == Integer.parseInt(Config.getValue("ss-attachments"))) {
                final Member member = Utils.getArg(args2[1], g2);
                if (member == null) {
                    BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                    return;
                }
                final String ID2 = member.getId();
                Object content = "";
                for (String string : args2) {
                    content = (String)content + string + " ";
                }
                String reason = ((String)content).replaceAll(args2[0], "").replaceAll(args2[1], "").trim();
                Role freezeRole = RBW.frozenRole;
                if (ScreenShare.getByScreenShared(ID2) != null) {
                    BetterEmbed error = new BetterEmbed("error", "Error", "", "This player is already getting screen-shared, \nIf you believe this is an error please contact a developer.", "");
                    error.reply(msg);
                    return;
                }
                if (freezeRole == null) {
                    BetterEmbed error = new BetterEmbed("error", "Critical Error", "", "Report this to the owner (FREEZE_ROLE_NOT_SET)", "");
                    error.reply(msg);
                    return;
                }
                if (m3 == member) {
                    BetterEmbed error = new BetterEmbed("error", "", "", Messages.SS_SELF.get(), "");
                    error.reply(msg);
                    return;
                }
                g2.addRoleToMember(member, freezeRole).queue();
                Object roles = "";
                for (String s2 : Config.getValue("ss-roles").split(",")) {
                    roles = (String)roles + g2.getRoleById(s2).getAsMention();
                }
                Category category = RBW.ssCategory;
                final TextChannel SSChannel = (TextChannel)category.createTextChannel("ss-" + Player.getName(ID2)).complete();
                new ScreenShare(SSChannel.getId(), ID2);
                new Transcript(SSChannel.getId());
                SSChannel.upsertPermissionOverride(member).setAllowed(Permission.VIEW_CHANNEL).queue();
                BetterEmbed embed = new BetterEmbed("info", "\uD83D\uDD0D  Screenshare Request", "", "", "");
                embed.setDescription("Use \u2705 to accept or \u274c to deny the request \n\n**Target:** " + member.getAsMention() + "\n**Requested by:** " + m3.getAsMention() + "\n**Reason:** `" + reason + "`");
                embed.setImage(attachments.get(0).getUrl());
                ((MessageCreateAction)((MessageCreateAction)SSChannel.sendMessage(member.getAsMention() + " " + (String)roles).setEmbeds(embed.build())).setActionRow(SS.buttons(member.getId()))).queue();
                final Role finalFreezeRole = freezeRole;
                TimerTask task = new TimerTask(){

                    @Override
                    public void run() {
                        ScreenShare ss = ScreenShare.getByScreenShared(ID2);
                        if (ss == null || ss.getScreenSharer() == null) {
                            g2.removeRoleFromMember(member, finalFreezeRole).queue();
                            SSChannel.sendMessage("Since nobody accepted the screenshare, you are good to go " + member.getAsMention()).queue();
                            SSChannel.delete().queueAfter(5L, TimeUnit.SECONDS);
                            if (ss != null) {
                                ss.delete();
                            }
                            TextChannel logs = RBW.ssLogsChannel;
                            BetterEmbed embed1 = new BetterEmbed("info", "\uD83D\uDCC1  SS Closed", "", "Reason: **No one accepted**\nRequested by: " + m3.getAsMention() + "\nTarget: " + member.getAsMention(), "");
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
                };
                Timer timer = new Timer();
                timer.schedule(task, (long)Integer.parseInt(Config.getValue("time-till-unfrozen")) * 60000L);
                BetterEmbed success = new BetterEmbed("info", "\uD83D\uDD0D  Screenshare Request Created", "", "The screenshare request for " + member.getAsMention() + " has been created in " + SSChannel.getAsMention(), "");
                success.reply(msg);
            } else {
                BetterEmbed error = new BetterEmbed("warning", "", "", "Please attach **" + Config.getValue("ss-attachments") + "** image(s) as proof.", "");
                error.reply(msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

