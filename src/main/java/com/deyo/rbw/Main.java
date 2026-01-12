/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.LeaderboardTask;
import com.deyo.rbw.childclasses.Msg;
import com.deyo.rbw.childclasses.Perms;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.EloBounties;
import com.deyo.rbw.classes.EloDecays;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.guild.Guild;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.classes.screenshare.ScreenShare;
import com.deyo.rbw.commands.CommandManager;
import com.deyo.rbw.commands.SlashCommandsManager;
import com.deyo.rbw.events.ButtonsEvent;
import com.deyo.rbw.events.HelpEvent;
import com.deyo.rbw.events.JoinEvent;
import com.deyo.rbw.events.QueueEvent;
import com.deyo.rbw.events.SlashCommandsEvent;
import com.deyo.rbw.ingame.sync.RoleSync;
import com.deyo.rbw.ingame.sync.TagSync;

public class Main {
    public static JDA jda;
    public static String version;
    public static boolean lessCpu;
    public static boolean valid;
    private static ScheduledExecutorService licenseScheduler;

    public static void runTaskLater(final Runnable task, long time) {
        // Always run async to avoid blocking main thread
        new Thread(() -> {
            try {
                Thread.sleep(time);
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args2) {
        Runtime.getRuntime().addShutdownHook(new Thread(Main::onDisable));
        System.setProperty("http.agent", "Chrome");
        try {
            Config.loadConfig();
            new File("RBW/players").mkdirs();
            new File("RBW/ranks").mkdirs();
            new File("RBW/bans").mkdirs();
            new File("RBW/queues").mkdirs();
            new File("RBW/games").mkdirs();
            new File("RBW/themes").mkdirs();
            new File("RBW/transcripts").mkdirs();
            new File("RBW/maps").mkdirs();
            new File("RBW/guilds").mkdirs();
            new File("RBW/screenshares").mkdirs();
            Perms.loadPerms();
            Msg.loadMsg();
            Player.loadPlayers();
            Rank.loadRanks();
            Game.loadGames();
            Guild.loadGuilds();
            ScreenShare.loadScreenShares();
            Queue.loadQueues();
            Themes.load();
            EloBounties.load();
            EloDecays.load();
            BetterEmbed.loadColors();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(0, new File("RBW/fonts/stats.ttf")));
            CommandManager.prefix = Config.getValue("prefix");
            JDABuilder jdaBuilder = JDABuilder.createDefault(Config.getValue("token"));
            jdaBuilder.setStatus(OnlineStatus.valueOf(Config.getValue("status")));
            jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
            jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, new GatewayIntent[0]);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES, new GatewayIntent[0]);
            jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT, new GatewayIntent[0]);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_VOICE_STATES, new GatewayIntent[0]);
            jdaBuilder.addEventListeners(new HelpEvent(), new CommandManager(), new SlashCommandsEvent(), new ButtonsEvent(), new QueueEvent(), new JoinEvent());
            jda = jdaBuilder.build();
            try {
                SlashCommandsManager.registerCMDs(jda);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            SlashCommandsManager.load();
            if (!new File("RBW/serverstats.yml").exists()) {
                BufferedWriter bw = new BufferedWriter(new FileWriter("RBW/serverstats.yml"));
                bw.write("games-played: 0");
                bw.close();
            }
            Bukkit.getConsoleSender().sendMessage("\u00a7b\u00a7m=================================================");
            Bukkit.getConsoleSender().sendMessage("                     \u00a7b\u00a7lRBW");
            Bukkit.getConsoleSender().sendMessage("                   \u00a7eversion: \u00a72v" + version);
            Bukkit.getConsoleSender().sendMessage("          \u00a7athe bot has successfully started");
            Bukkit.getConsoleSender().sendMessage("\u00a7b\u00a7m=================================================");
            jda.awaitReady();
            RBW.load(jda);
            if (!Bukkit.getPluginManager().isPluginEnabled((Plugin)com.deyo.rbw.ingame.Main.instance)) {
                return;
            }
            Parties.load();
            LeaderboardTask.initTask();
            com.deyo.rbw.features.MonthlyRecapTask.init(jda);
            RoleSync.load();
            TagSync.load();
            Thread periodicSaveThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(60000L);
                        Main.saveData();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            periodicSaveThread.setDaemon(true);
            periodicSaveThread.start();
            lessCpu = Config.getConfig().getBoolean("less-cpu-mode");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveData() {
        new Thread(() -> {
            try {
                System.out.println("Saving data (players, ranks etc)");
                Player.saveData();
                System.out.println("Players data successfully saved");
                Rank.saveData();
                System.out.println("Ranks data successfully saved");
                Game.saveData();
                System.out.println("Games data successfully saved");
                Queue.saveData();
                QueueEvent.ServerConfig.save();
                System.out.println("Queues data successfully saved");
                Guild.saveData();
                System.out.println("Guilds data successfully saved");
                ScreenShare.saveData();
                System.out.println("Screenshare data successfully saved");
                EloDecays.save();
            } catch (Exception e) {
                System.err.println("[RBW] Error during data save: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public static void setupHeartbeatScheduler(String licenseKey) {
        licenseScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "License-Heartbeat");
            t.setDaemon(true);
            return t;
        });
        
        licenseScheduler.scheduleAtFixedRate(() -> {
            try {
                License.sendHeartbeat(licenseKey);
            } catch (Exception e) {
            }
        }, 15, 15, TimeUnit.MINUTES);
        
        System.out.println("[License] Heartbeat scheduler configured to send requests every 15 minutes");
    }

    public static void onDisable() {
        if (licenseScheduler != null && !licenseScheduler.isShutdown()) {
            System.out.println("[License] Shutting down heartbeat scheduler...");
            licenseScheduler.shutdown();
            try {
                if (!licenseScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    licenseScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                licenseScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println("[License] Heartbeat scheduler stopped");
        }
        Main.saveData();
        System.out.println("[RBW] Plugin has been disabled!");
    }

    static {
        version = "2.0";
    }
}

