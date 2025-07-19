/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.childclasses;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.ingame.Main;

public class RBW {
    public static Guild mainGuild;
    public static RichCustomEmoji commandEmoji;
    public static TextChannel alertsChannel;
    public static TextChannel gameLogsChannel;
    public static TextChannel scoringLogsChannel;
    public static TextChannel banChannel;
    public static TextChannel ssLogsChannel;
    public static TextChannel gamesAnnouncingChannel;
    public static TextChannel scoredAnnouncingChannel;
    public static TextChannel bountiesChannel;
    public static TextChannel eloDecayAnnouncementsChannel;
    public static TextChannel leaderboardUpdatesChannel;
    public static Category gameChannelsCategory;
    public static Category gameVcsCategory;
    public static Category ssCategory;
    public static VoiceChannel waitingRoom;
    public static Role registeredRole;
    public static Role prefixToggleRole;
    public static Role bannedRole;
    public static Role frozenRole;
    public static Role themesManagerRole;

    public static void load(JDA jda) {
        mainGuild = jda.getGuildById(Config.getValue("guild"));
        if (mainGuild == null) {
            Bukkit.getConsoleSender().sendMessage("\u00a7cCouldn't load main guild. Please configure it correctly.");
            Bukkit.getPluginManager().disablePlugin((Plugin)Main.instance);
            return;
        }
        commandEmoji = mainGuild.getEmojiById(Config.getValue("command-emoji"));
        waitingRoom = mainGuild.getVoiceChannelById(Config.getValue("waiting-room"));
        alertsChannel = mainGuild.getTextChannelById(Config.getValue("alerts-channel"));
        gameLogsChannel = mainGuild.getTextChannelById(Config.getValue("game-logs"));
        scoringLogsChannel = mainGuild.getTextChannelById(Config.getValue("scoring-logs-channel"));
        banChannel = mainGuild.getTextChannelById(Config.getValue("ban-channel"));
        ssLogsChannel = mainGuild.getTextChannelById(Config.getValue("ss-logs-channel"));
        gamesAnnouncingChannel = mainGuild.getTextChannelById(Config.getValue("games-announcing"));
        scoredAnnouncingChannel = mainGuild.getTextChannelById(Config.getValue("scored-announcing"));
        bountiesChannel = mainGuild.getTextChannelById(Config.getValue("bounties-channel"));
        eloDecayAnnouncementsChannel = mainGuild.getTextChannelById(Config.getValue("elo-decay.announcements"));
        leaderboardUpdatesChannel = mainGuild.getTextChannelById(Config.getValue("leaderboard-updates"));
        ssCategory = mainGuild.getCategoryById(Config.getValue("ss-category"));
        gameChannelsCategory = mainGuild.getCategoryById(Config.getValue("game-channels-category"));
        gameVcsCategory = mainGuild.getCategoryById(Config.getValue("game-vcs-category"));
        prefixToggleRole = mainGuild.getRoleById(Config.getValue("prefix-toggle-role"));
        registeredRole = mainGuild.getRoleById(Config.getValue("registered-role"));
        bannedRole = mainGuild.getRoleById(Config.getValue("banned-role"));
        frozenRole = mainGuild.getRoleById(Config.getValue("frozen-role"));
        themesManagerRole = mainGuild.getRoleById(Config.getValue("themesmanager"));
    }

    static {
        commandEmoji = null;
        alertsChannel = null;
        gameLogsChannel = null;
        scoringLogsChannel = null;
        banChannel = null;
        ssLogsChannel = null;
        gamesAnnouncingChannel = null;
        scoredAnnouncingChannel = null;
        bountiesChannel = null;
        eloDecayAnnouncementsChannel = null;
        leaderboardUpdatesChannel = null;
        gameChannelsCategory = null;
        gameVcsCategory = null;
        ssCategory = null;
        waitingRoom = null;
        registeredRole = null;
        prefixToggleRole = null;
        bannedRole = null;
        frozenRole = null;
        themesManagerRole = null;
    }
}

