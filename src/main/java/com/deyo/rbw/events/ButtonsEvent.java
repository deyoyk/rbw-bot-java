/*
 * Recoded by deyo 
 */
package com.deyo.rbw.events;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.classes.screenshare.ScreenShare;
import com.deyo.rbw.commands.CommandManager;
import com.deyo.rbw.commands.commands.banningsystem.Strike;
import com.deyo.rbw.commands.commands.gamesystem.Games;
import com.deyo.rbw.commands.commands.player.guild.GuildJoin;
import com.deyo.rbw.commands.commands.player.party.PartyJoin;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.Statistic;

public class ButtonsEvent
extends ListenerAdapter {
    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        String channelID = event.getChannel().getId();
        for (ScreenShare screenShare : ScreenShare.screenShares) {
            if (!screenShare.getChannelID().equalsIgnoreCase(channelID)) continue;
            screenShare.delete();
            break;
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        ScreenShare ss;
        String ID2;
        event.deferEdit().queue();
        if (event.getButton().getId() == null) {
            return;
        }
        if (event.getButton().getId().contains("GAMES-")) {
            String s22 = null;
            GameState state;
            ID2 = event.getButton().getId().split("-")[1];
            String stat = event.getButton().getId().split("-")[2];
            if (stat.equalsIgnoreCase("null")) {
                state = null;
            } else {
                try {
                    state = GameState.valueOf(stat);
                }
                catch (Exception ex) {
                    return;
                }
            }
            int page = 1;
            if (event.getButton().getId().contains("previous")) {
                try {
                    s22 = event.getMessage().getEmbeds().get(0).getFooter().getText().split(": ")[1];
                    page = Integer.parseInt(s22);
                    if (--page < 1) {
                        page = 1;
                    }
                }
                catch (Exception ex2) {
                    // empty catch block
                }
            }
            if (event.getButton().getId().contains("next")) {
                try {
                    s22 = event.getMessage().getEmbeds().get(0).getFooter().getText().split(": ")[1];
                    page = Integer.parseInt(s22);
                    ++page;
                }
                catch (Exception ex3) {
                    // empty catch block
                }
            }
            ((MessageEditAction)event.getMessage().editMessageEmbeds(Games.showGames(ID2, page, state).build()).setActionRow(Games.buttons(ID2, state))).queue();
        }
        if (event.getButton().getId().equals("previousLB-" + event.getMember().getId())) {
            this.previouslbpage(event.getMessage(), event.getMember().getId());
        } else if (event.getButton().getId().equals("nextLB-" + event.getMember().getId())) {
            this.nextlbpage(event.getMessage(), event.getMember().getId());
        } else if (event.getButton().getId().equals("acceptguild-" + event.getMember().getId())) {
            event.getChannel().sendMessage(event.getMember().getAsMention()).queue();
            GuildJoin.execute(new String[]{"=guild", "join"}, event.getGuild(), event.getMember(), event.getChannel(), new CommandAdapter(event.getMessage()), "");
        } else if (event.getButton().getId().equals("accept-" + event.getMember().getId())) {
            event.getChannel().sendMessage(event.getMember().getAsMention()).queue();
            PartyJoin.execute(new String[]{"=party", "join"}, event.getGuild(), event.getMember(), event.getChannel(), new CommandAdapter(event.getMessage()), "");
        } else if (event.getButton().getId().startsWith("confirmStrike:")) {
            if (!CommandManager.hasGroup("staff", event.getMember(), event.getGuild())) {
                return;
            }
            String[] parts = event.getButton().getId().split(":");
            if (parts.length < 3) {
                return;
            }
            String targetId = parts[1];
            String strikerId = parts[2];
            if (!strikerId.equals(event.getMember().getId())) {
                return;
            }
            String strikeKey = targetId + ":" + strikerId;
            String reason = Strike.pendingStrikes.get(strikeKey);
            if (reason == null) {
                BetterEmbed error = new BetterEmbed("error", "Error", "", "Strike confirmation expired or invalid.", "");
                event.getMessage().editMessageEmbeds(error.build()).setComponents().queue();
                return;
            }
            try {
                Member targetMember = event.getGuild().getMemberById(targetId);
                if (targetMember == null) {
                    BetterEmbed error = new BetterEmbed("error", "Error", "", "Target member not found.", "");
                    event.getMessage().editMessageEmbeds(error.build()).setComponents().queue();
                    return;
                }
                Strike.strike(targetId, event.getGuild(), event.getMember(), targetMember, reason, new CommandAdapter(event.getMessage()));
                Strike.pendingStrikes.remove(strikeKey);
                event.getMessage().editMessage("✅ Strike confirmed and issued.").setComponents().queue();
            } catch (Exception e) {
                e.printStackTrace();
                BetterEmbed error = new BetterEmbed("error", "Error", "", "Failed to issue strike: " + e.getMessage(), "");
                event.getMessage().editMessageEmbeds(error.build()).setComponents().queue();
            }
        } else if (event.getButton().getId().startsWith("cancelStrike:")) {
            if (!CommandManager.hasGroup("staff", event.getMember(), event.getGuild())) {
                return;
            }
            String[] parts = event.getButton().getId().split(":");
            if (parts.length < 3) {
                return;
            }
            String targetId = parts[1];
            String strikerId = parts[2];
            if (!strikerId.equals(event.getMember().getId())) {
                return;
            }
            String strikeKey = targetId + ":" + strikerId;
            Strike.pendingStrikes.remove(strikeKey);
            event.getMessage().editMessage("❌ Strike cancelled.").setComponents().queue();
        } else if (event.getButton().getId().contains("Accept-SS::")) {
            if (!CommandManager.hasGroup("staff", event.getMember(), event.getGuild())) {
                return;
            }
            ID2 = event.getButton().getId().split("::")[1];
            ss = ScreenShare.getByScreenShared(ID2);
            if (ss == null || ss.getScreenSharer() != null) {
                return;
            }
            BetterEmbed error1 = new BetterEmbed("success", "Ranked BedWars Moderation", "", "<@" + ID2 + "> **is now frozen.**\n\nPlease follow the SS instructions given below\nIf you admit to cheating , you will receive a shorter ban of only 10d if not,\nDownload AnyDesk and send the code within 7 minutes\nhttps://anydesk.com/en/downloads/windows\n\n**Tips**\n```py\n1. Do not Log or turn off any Application\n2. Do not Plug/unplug any peripheral device such as mouse/keyboard\n3. Do not Rename\\Delete\\Modify Any File\n```", "");
            ((MessageCreateAction)event.getMessage().reply("<@" + ID2 + ">, " + event.getMember().getAsMention()).setEmbeds(error1.build())).queue();
            Statistic.SCREENSHARED.setForPlayer(event.getMember().getId(), Statistic.SCREENSHARED.getForPlayer(event.getMember().getId()) + 1.0);
            ss.setScreenSharer(event.getMember().getId());
        } else if (event.getButton().getId().contains("Deny-SS::")) {
            if (!CommandManager.hasGroup("staff", event.getMember(), event.getGuild())) {
                return;
            }
            ID2 = event.getButton().getId().split("::")[1];
            ss = ScreenShare.getByScreenShared(ID2);
            if (ss == null || ss.getScreenSharer() != null) {
                return;
            }
            event.getMessage().reply(event.getMember().getAsMention() + ", You denied the SS Request\n :wastebasket: \ufe0f Transcript saving, This channel will be deleted in 10 seconds").queue();
            TextChannel channel = event.getGuild().getTextChannelById(ss.getChannelID());
            if (channel == null) {
                return;
            }
            Role freezeRole = RBW.frozenRole;
            if (freezeRole == null) {
                return;
            }
            ss.delete();
            event.getGuild().removeRoleFromMember(event.getGuild().getMemberById(ID2), freezeRole).queue();
            channel.delete().queueAfter(10L, TimeUnit.SECONDS);
            TextChannel logs = RBW.ssLogsChannel;
            BetterEmbed embed1 = new BetterEmbed("default", "SS Closed", "", "Reason: **Denied by **" + event.getMember().getAsMention() + "\nTarget: <@" + ID2 + ">", "");
            logs.sendMessageEmbeds(embed1.build(), new MessageEmbed[0]).queue();
            Transcript transcript = Transcript.getTranscript(ss.getChannelID());
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

    public void previouslbpage(final Message message, String ID2) {
        if (message.getEmbeds().get(0).getTitle().equals("Error")) {
            return;
        }
        String footerText = message.getEmbeds().get(0).getFooter().getText();
        int page = 1;
        try {
            if (footerText.contains("**")) {
                String pageStr = footerText.replaceAll(".*\\*\\*", "").replaceAll("\\*\\*.*", "");
                page = Integer.parseInt(pageStr.trim());
            } else if (footerText.contains(":")) {
                page = Integer.parseInt(footerText.split(":")[1].trim());
            }
        } catch (Exception ex) {
            page = 1;
        }
        if (page != 1) {
            --page;
            String title = message.getEmbeds().get(0).getTitle();
            String stat = "";
            if (title.contains("Leaderboard")) {
                String beforeLeaderboard = title.split("Leaderboard")[0].trim();
                String[] parts = beforeLeaderboard.split("\\s+");
                if (parts.length > 0) {
                    stat = parts[parts.length - 1].toLowerCase();
                }
            }
            if (stat.isEmpty()) {
                String[] titleParts = title.split("\\s+");
                if (titleParts.length > 0) {
                    stat = titleParts[0].trim().toLowerCase();
                }
            }
            DecimalFormat formatter = new DecimalFormat("#0");
            List<Map.Entry<String, Double>> list = ButtonsEvent.getList(stat);
            if (list == null) {
                return;
            }
            if (stat.equals("wlr") || stat.equals("kdr")) {
                formatter = new DecimalFormat("#0.00");
            }
            if ((page - 1) * 10 < list.size() && page > 0) {
                Object lb = "";
                for (int i = (page - 1) * 10; i < page * 10; ++i) {
                    if (i >= list.size()) continue;
                    String[] values2 = list.get(i).toString().split("=");
                    int place = i + 1;
                    lb = place == 1 ? (String)lb + ":first_place: `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n" : (place == 2 ? (String)lb + ":second_place: `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n" : (place == 3 ? (String)lb + ":third_place: `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n" : (String)lb + "**#" + place + "** `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n"));
                }
                BetterEmbed embed = new BetterEmbed("info", "\uD83C\uDFC6  " + Utils.formatName(stat) + " Leaderboard", "", (String)lb, "Page **" + page + "**");
                ((MessageEditAction)message.editMessageEmbeds(embed.build()).setActionRow(ButtonsEvent.buttons(ID2))).queue();
            } else {
                final MessageEmbed eb = message.getEmbeds().get(0);
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "This page doesn't exist", "");
                message.editMessageEmbeds(embed.build()).queue();
                TimerTask task = new TimerTask(){

                    @Override
                    public void run() {
                        message.editMessageEmbeds(eb).queue();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 3000L);
            }
        } else {
            final MessageEmbed eb = message.getEmbeds().get(0);
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "You are already on the first page", "");
            message.editMessageEmbeds(embed.build()).queue();
            TimerTask task = new TimerTask(){

                @Override
                public void run() {
                    message.editMessageEmbeds(eb).queue();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 3000L);
        }
    }

    public void nextlbpage(final Message message, String ID2) {
        if (message.getEmbeds().get(0).getTitle().equals("Error")) {
            return;
        }
        String footerText = message.getEmbeds().get(0).getFooter().getText();
        int page = 1;
        try {
            if (footerText.contains("**")) {
                String pageStr = footerText.replaceAll(".*\\*\\*", "").replaceAll("\\*\\*.*", "");
                page = Integer.parseInt(pageStr.trim());
            } else if (footerText.contains(":")) {
                page = Integer.parseInt(footerText.split(":")[1].trim());
            }
        } catch (Exception ex) {
            page = 1;
        }
        ++page;
        String title = message.getEmbeds().get(0).getTitle();
        String stat = "";
        if (title.contains("Leaderboard")) {
            String beforeLeaderboard = title.split("Leaderboard")[0].trim();
            String[] parts = beforeLeaderboard.split("\\s+");
            if (parts.length > 0) {
                stat = parts[parts.length - 1].toLowerCase();
            }
        }
        if (stat.isEmpty()) {
            String[] titleParts = title.split("\\s+");
            if (titleParts.length > 0) {
                stat = titleParts[0].trim().toLowerCase();
            }
        }
        DecimalFormat formatter = new DecimalFormat("#0");
        List<Map.Entry<String, Double>> list = ButtonsEvent.getList(stat);
        if (list == null) {
            return;
        }
        if (stat.equals("wlr") || stat.equals("kdr")) {
            formatter = new DecimalFormat("#0.00");
        }
        if ((page - 1) * 10 < list.size() && page > 0) {
            Object lb = "";
            for (int i = (page - 1) * 10; i < page * 10; ++i) {
                if (i >= list.size()) continue;
                String[] values2 = list.get(i).toString().split("=");
                int place = i + 1;
                lb = place == 1 ? (String)lb + ":first_place: `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n" : (place == 2 ? (String)lb + ":second_place: `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n" : (place == 3 ? (String)lb + ":third_place: `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n" : (String)lb + "**#" + place + "** `" + Player.getName(values2[0]) + "` \u2014 " + formatter.format(Double.parseDouble(values2[1])) + "\n"));
            }
            BetterEmbed embed = new BetterEmbed("info", "\uD83C\uDFC6  " + Utils.formatName(stat) + " Leaderboard", "", (String)lb, "Page **" + page + "**");
            ((MessageEditAction)message.editMessageEmbeds(embed.build()).setActionRow(ButtonsEvent.buttons(ID2))).queue();
        } else {
            final MessageEmbed eb = message.getEmbeds().get(0);
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "This page doesn't exist", "");
            message.editMessageEmbeds(embed.build()).queue();
            TimerTask task = new TimerTask(){

                @Override
                public void run() {
                    message.editMessageEmbeds(eb).queue();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 3000L);
        }
    }

    public static List<Map.Entry<String, Double>> getList(String stat) {
        HashMap<String, Double> unsortedMap = new HashMap<String, Double>();
        Statistic statistic = Statistic.getFromString(stat);
        if (statistic == null) {
            return null;
        }
        for (String ID2 : Player.getPlayers().keySet()) {
            unsortedMap.put(ID2, statistic.getForPlayer(ID2));
        }
        Map<String, Double> sortedMap = Utils.sortByValue(unsortedMap);
        return new LinkedList<Map.Entry<String, Double>>(sortedMap.entrySet());
    }

    private static List<Button> buttons(String ID2) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(Button.secondary("previousLB-" + ID2, "\u2190"));
        buttons.add(Button.secondary("nextLB-" + ID2, "\u2192"));
        return buttons;
    }
}

