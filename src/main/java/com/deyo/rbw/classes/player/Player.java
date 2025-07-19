/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.classes.player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CachedPlayer;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.EloDecays;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.StatChangelog;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.PlayerStoring;
import com.deyo.rbw.commands.commands.banningsystem.Ban;
import com.deyo.rbw.commands.types.Statistic;

import org.bukkit.plugin.java.JavaPlugin;

public class Player {
    static HashMap<String, CachedPlayer> playersCache = new HashMap();
    static HashMap<String, PlayerStoring> players = new HashMap();
    static HashMap<String, String> idByName = new HashMap();
    static Role noformat = null;
    private static final Object playerLock = new Object();
    private static Timer periodicSaveTimer = null;

    public static HashMap<String, CachedPlayer> getPlayersCache() {
        return playersCache;
    }

    public static void loadPlayers() {
        File playersfolder = new File("RBW/players");
        if (playersfolder.listFiles().length > 0) {
            for (File f : playersfolder.listFiles()) {
                String id = f.getName().replaceAll(".yml", "");
                PlayerStoring p = new PlayerStoring(id, f);
                players.put(id, p);
                idByName.put(p.getConfig().getString("name", "unknownnnn"), id);
            }
            System.out.println("Successfully loaded all players into memory");
        }
    }

    public static void saveData() {
        try {
            for (PlayerStoring player : players.values()) {
                player.save(); 
                EloDecays.check(player.getID());
                long unbanned;
                String ID2 = player.getID();
                if (!Player.isBanned(ID2) || (unbanned = YamlConfiguration.loadConfiguration(new File("RBW/bans/" + ID2 + ".yml")).getLong("unbanned")) >= System.currentTimeMillis()) continue;
                Player.unban(RBW.mainGuild, ID2);
                BetterEmbed embed = new BetterEmbed("success", "Ranked BedWars Moderation", RBW.mainGuild.getIconUrl(), "You have been unbanned, Feel free to play now!", "");
                embed.addField("User", "<@" + ID2 + ">", true);
                embed.addField("Moderator", RBW.mainGuild.getJDA().getSelfUser().getAsMention(), true);
                embed.addField("Reason", "`Ban Expired`", true);
                if (RBW.banChannel != null) {
                    RBW.banChannel.sendMessage("<@" + ID2 + ">").queue();
                    RBW.banChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
                }
                Role bannedRole = RBW.bannedRole;
                Member m3 = RBW.mainGuild.getMemberById(ID2);
                if (m3 == null || bannedRole == null) continue;
                RBW.mainGuild.removeRoleFromMember(m3, bannedRole).queue();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, PlayerStoring> getPlayers() {
        return players;
    }

    public static void createFile(String ID2, String ign) {
        File file = new File("RBW/players/" + ID2 + ".yml");
        try {
            Member m3;
            file.createNewFile();
            YamlConfiguration playerTempDB = YamlConfiguration.loadConfiguration(file);
            playerTempDB.set("name", ign);
            for (Statistic stat : Statistic.values()) {
                if (stat == Statistic.ELO || stat == Statistic.PEAKELO) {
                    playerTempDB.set(stat.getPath(), Config.getConfig().getInt("starting-elo"));
                    continue;
                }
                playerTempDB.set(stat.getPath(), 0);
            }
            playerTempDB.set("theme", "default");
            playerTempDB.set("owned-themes", "default");
            playerTempDB.save(file);
            PlayerStoring player = new PlayerStoring(ID2, file);
            players.put(ID2, player);
            idByName.put(ign, ID2);
            if (RBW.mainGuild != null && (m3 = RBW.mainGuild.getMemberById(ID2)) != null && m3.getTimeCreated().isAfter(OffsetDateTime.now().minusMonths(1L))) {
                Ban.ban(RBW.mainGuild, ID2, "90d", "Suspected alt. Please open a ticket if you believe this is false.", m3, RBW.mainGuild.getSelfMember());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned(String id) {
        return new File("RBW/bans/" + id + ".yml").exists();
    }

    public static String getIdFromIGN(String ign) {
        if (idByName.containsKey(ign)) {
            return idByName.get(ign);
        }
        for (Map.Entry<String, PlayerStoring> entry : players.entrySet()) {
            String name = entry.getValue().getConfig().getString("name");
            if (name == null) continue; // Prevent NPE
            if (name.equalsIgnoreCase(ign)) return entry.getKey();
        }
        return null;
    }

    public static void fix(String ID2, Guild g2) {
        block14: {
            try {
                ArrayList<Role> rolestoremove = new ArrayList<Role>();
                ArrayList<Role> rolestoadd = new ArrayList<Role>();
                String name = Player.getName(ID2);
                int elo = (int)Statistic.ELO.getForPlayer(ID2);
                rolestoadd.add(RBW.registeredRole);
                Member m3 = g2.getMemberById(ID2);
                for (Rank rank : Rank.getRanks()) {
                    int startingElo = rank.getStartingElo();
                    int endingElo = rank.getEndingElo();
                    String s2 = rank.getId();
                    if (elo < startingElo || elo > endingElo) continue;
                    rolestoadd.add(g2.getRoleById(s2));
                    for (Rank rank2 : Rank.getRanks()) {
                        if (rank2.getId().equals(s2)) continue;
                        rolestoremove.add(g2.getRoleById(rank2.getId()));
                    }
                }
                if (Player.isBanned(ID2)) {
                    rolestoadd.add(RBW.bannedRole);
                } else {
                    rolestoremove.add(RBW.bannedRole);
                }
                if (m3 == null) break block14;
                g2.modifyMemberRoles(m3, rolestoadd, rolestoremove).queue();
                try {
                    if (noformat == null) {
                        try {
                            noformat = RBW.prefixToggleRole;
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    if (noformat != null && m3.getRoles().contains(noformat)) {
                        g2.modifyNickname(m3, name).queue();
                        return;
                    }
                    String n = Config.getValue("elo-formatting").replaceAll("%elo%", String.valueOf(elo)) + name;
                    if (!Player.getNick(ID2).isEmpty()) {
                        n = n + " | " + Player.getNick(ID2);
                    }
                    if (m3.getEffectiveName().equals(n)) {
                        return;
                    }
                    g2.modifyNickname(m3, n).queue();
                }
                catch (Exception exception) {}
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void wipe(String ID2) {
        try {
            PlayerStoring pa = players.get(ID2);
            for (Statistic value : Statistic.values()) {
                if (value == Statistic.ELO || value == Statistic.PEAKELO) {
                    value.setForPlayer(ID2, Integer.parseInt(Config.getValue("starting-elo")));
                    continue;
                }
                value.setForPlayer(ID2, 0.0);
            }
            pa.getConfig().set("theme", "default");
            pa.getConfig().set("owned-themes", "default");
            pa = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPlayer(String ID2) {
        return players.get(ID2) != null;
    }

    public static String getNick(String ID2) {
        return players.get(ID2).getConfig().getString("nick", "");
    }

    public static String getName(String ID2) {
        return players.get(ID2).getConfig().getString("name");
    }

    public static String getTheme(String ID2) {
        return players.get(ID2).getConfig().getString("theme");
    }

    public static String getOwnedThemes(String ID2) {
        return players.get(ID2).getConfig().getString("owned-themes");
    }

    public static void setName(String ID2, String newName) {
        idByName.remove(Player.getName(ID2));
        players.get(ID2).getConfig().set("name", newName);
        idByName.put(newName, ID2);
        if (Main.lessCpu) {
            playersCache.remove(ID2);
        }
    }

    public static void setNick(String ID2, String nick) {
        players.get(ID2).getConfig().set("nick", nick);
    }

    public static void setTheme(String ID2, String theme) {
        players.get(ID2).getConfig().set("theme", theme);
    }

    public static void addTheme(String ID2, String theme) {
        Object h2 = Player.getOwnedThemes(ID2);
        h2 = (String)h2 + "," + theme;
        players.get(ID2).getConfig().set("owned-themes", h2);
    }

    public static void removeTheme(String ID2, String theme) {
        String h2 = Player.getOwnedThemes(ID2);
        h2 = h2.replaceAll("," + theme, "");
        players.get(ID2).getConfig().set("owned-themes", h2);
    }

    public static void updateWS(String ID2, Game game) {
        StatChangelog wsChangelog;
        int newWS = (int)(Statistic.WS.getForPlayer(ID2) + 1.0);
        if (game != null) {
            wsChangelog = new StatChangelog(ID2, Statistic.WS, 1);
            game.storeChangelog(wsChangelog);
        }
        if (Statistic.HIGHESTWS.getForPlayer(ID2) < (double)newWS) {
            if (game != null) {
                wsChangelog = new StatChangelog(ID2, Statistic.HIGHESTWS, (int)((double)newWS - Statistic.HIGHESTWS.getForPlayer(ID2)));
                game.storeChangelog(wsChangelog);
            }
            Statistic.HIGHESTWS.setForPlayer(ID2, newWS);
        }
        Statistic.WS.setForPlayer(ID2, newWS);
        if (game != null) {
            StatChangelog lsChangelog = new StatChangelog(ID2, Statistic.LS, (int)(-Statistic.LS.getForPlayer(ID2)));
            game.storeChangelog(lsChangelog);
        }
        Statistic.LS.setForPlayer(ID2, 0.0);
    }

    public static void updateLS(String ID2, Game game) {
        StatChangelog lsChangelog;
        int newLS = (int)(Statistic.LS.getForPlayer(ID2) + 1.0);
        if (game != null) {
            lsChangelog = new StatChangelog(ID2, Statistic.LS, 1);
            game.storeChangelog(lsChangelog);
        }
        if (Statistic.HIGHESTLS.getForPlayer(ID2) < (double)newLS) {
            if (game != null) {
                lsChangelog = new StatChangelog(ID2, Statistic.HIGHESTLS, (int)((double)newLS - Statistic.HIGHESTLS.getForPlayer(ID2)));
                game.storeChangelog(lsChangelog);
            }
            Statistic.HIGHESTLS.setForPlayer(ID2, newLS);
        }
        Statistic.LS.setForPlayer(ID2, newLS);
        if (game != null) {
            StatChangelog wsChangelog = new StatChangelog(ID2, Statistic.WS, (int)(-Statistic.WS.getForPlayer(ID2)));
            game.storeChangelog(wsChangelog);
        }
        Statistic.WS.setForPlayer(ID2, 0.0);
    }

    public static long ban(final Guild g2, final String ID2, String duration, String reason) {
        try {
            final ArrayList banned = new ArrayList();
            if (RBW.bannedRole != null) {
                g2.addRoleToMember((UserSnowflake)g2.retrieveMemberById(ID2).complete(), RBW.bannedRole).queue();
            }
            Bukkit.getScheduler().runTask((Plugin)com.deyo.rbw.ingame.Main.instance, () -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("ban " + Player.getName(ID2) + " " + duration + " Banned On discord (" + reason + ")")));
            TimerTask task = new TimerTask(){

                @Override
                public void run() {
                    try {
                        YamlConfiguration banData;
                        long unbanned;
                        if (Player.isBanned(ID2) && (unbanned = (banData = YamlConfiguration.loadConfiguration(new File("RBW/bans/" + ID2 + ".yml"))).getLong("unbanned")) < System.currentTimeMillis()) {
                            g2.modifyMemberRoles((Member)g2.retrieveMemberById(ID2).complete(), null, banned).queue();
                            Player.unban(g2, ID2);
                            BetterEmbed embed = new BetterEmbed("success", "Ranked BedWars Moderation", Utils.avatar(g2.getIconUrl()), "You have been unbanned, Feel free to play now!", "");
                            embed.addField("User", "<@" + ID2 + ">", true);
                            embed.addField("Moderator", g2.getJDA().getSelfUser().getAsMention(), true);
                            embed.addField("Reason", "`Ban Expired`", true);
                            if (RBW.banChannel != null) {
                                RBW.banChannel.sendMessage("<@" + ID2 + ">").queue();
                                RBW.banChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            int time = Integer.parseInt(duration.replaceAll("[^0-9]", ""));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            YamlConfiguration data = YamlConfiguration.loadConfiguration(new File("RBW/bans/" + ID2 + ".yml"));
            try {
                data.set("banned", dtf.format(now));
                Timer timer = new Timer();
                long unban = System.currentTimeMillis();
                if (duration.contains("m")) {
                    unban += TimeUnit.MINUTES.toMillis(time);
                    timer.schedule(task, (long)(time + 1) * 60000L);
                } else if (duration.contains("h")) {
                    unban += TimeUnit.HOURS.toMillis(time);
                    timer.schedule(task, (long)(time + 1) * 3600000L);
                } else if (duration.contains("d")) {
                    unban += TimeUnit.DAYS.toMillis(time);
                    timer.schedule(task, (long)(time + 1) * 86400000L);
                } else {
                    unban += TimeUnit.SECONDS.toMillis(time);
                    timer.schedule(task, (long)(time + 1) * 1000L);
                }
                data.set("unbanned", unban);
                data.set("reason", reason);
                data.save(new File("RBW/bans/" + ID2 + ".yml"));
                return unban;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static void unban(Guild g2, String ID2) {
        Bukkit.getScheduler().runTask((Plugin)com.deyo.rbw.ingame.Main.instance, () -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("unban " + Player.getName(ID2) + " Unbanned From discord")));
        try {
            Files.deleteIfExists(Paths.get("RBW/bans/" + ID2 + ".yml", new String[0]));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Player.fix(ID2, g2);
    }

    public static List<Integer> lose(String ID2, Guild g2, int modifiers, Game game) {
        int oldElo;
        int newElo = oldElo = (int)Statistic.ELO.getForPlayer(ID2);
        int newgold = 30 + Utils.randomInt(1, 7);
        if (game != null) {
            StatChangelog goldChangelog = new StatChangelog(ID2, Statistic.GOLD, newgold);
            game.storeChangelog(goldChangelog);
        }
        Statistic.GOLD.setForPlayer(ID2, Statistic.GOLD.getForPlayer(ID2) + (double)newgold);
        try {
            for (Rank rank : Rank.getRanks()) {
                int startingElo = rank.getStartingElo();
                int endingElo = rank.getEndingElo();
                int loseElo = rank.getLoseElo();
                if (oldElo < startingElo || oldElo > endingElo) continue;
                if (modifiers > 0) {
                    loseElo = Math.round((float)(loseElo / 100) * (float)(100 - modifiers));
                }
                if (game != null) {
                    StatChangelog eloChangelog = new StatChangelog(ID2, Statistic.ELO, -loseElo);
                    game.storeChangelog(eloChangelog);
                }
                newElo = oldElo - loseElo;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Statistic.ELO.setForPlayer(ID2, newElo);
        if (game != null) {
            StatChangelog lossesChangelog = new StatChangelog(ID2, Statistic.LOSSES, 1);
            game.storeChangelog(lossesChangelog);
        }
        Statistic.LOSSES.setForPlayer(ID2, Statistic.LOSSES.getForPlayer(ID2) + 1.0);
        Player.updateLS(ID2, game);
        Player.fix(ID2, g2);
        ArrayList<Integer> losingvalues = new ArrayList<Integer>();
        losingvalues.add(oldElo);
        losingvalues.add(newElo);
        return losingvalues;
    }

    public static List<Integer> unlose(String ID2, Guild g2) {
        int oldElo;
        int newElo = oldElo = (int)Statistic.ELO.getForPlayer(ID2);
        try {
            for (Rank rank : Rank.getRanks()) {
                int startingElo = rank.getStartingElo();
                int endingElo = rank.getEndingElo();
                int loseElo = rank.getLoseElo();
                if (oldElo < startingElo || oldElo > endingElo) continue;
                if (oldElo >= loseElo) {
                    newElo = oldElo + loseElo;
                    continue;
                }
                newElo = 0;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Statistic.ELO.setForPlayer(ID2, newElo);
        Statistic.LS.setForPlayer(ID2, Statistic.LS.getForPlayer(ID2) - 1.0);
        Statistic.LOSSES.setForPlayer(ID2, Statistic.LOSSES.getForPlayer(ID2) - 1.0);
        Player.fix(ID2, g2);
        ArrayList<Integer> losingvalues = new ArrayList<Integer>();
        losingvalues.add(oldElo);
        losingvalues.add(newElo);
        return losingvalues;
    }

    public static List<Integer> win(String ID2, Guild g2, int modifiers, Game game) {
        int oldElo;
        int newElo = oldElo = (int)Statistic.ELO.getForPlayer(ID2);
        int newgold = 75 + Utils.randomInt(1, 15);
        if (game != null) {
            StatChangelog goldChangelog = new StatChangelog(ID2, Statistic.GOLD, newgold);
            game.storeChangelog(goldChangelog);
        }
        Statistic.GOLD.setForPlayer(ID2, Statistic.GOLD.getForPlayer(ID2) + (double)newgold);
        try {
            for (Rank rank : Rank.getRanks()) {
                int startingElo = rank.getStartingElo();
                int endingElo = rank.getEndingElo();
                int winElo = rank.getWinElo();
                if (oldElo < startingElo || oldElo > endingElo) continue;
                if (modifiers > 0) {
                    winElo = Math.round((float)(winElo / 100) * (float)(100 - modifiers));
                }
                if (game != null) {
                    StatChangelog eloChangelog = new StatChangelog(ID2, Statistic.ELO, winElo);
                    game.storeChangelog(eloChangelog);
                }
                newElo = oldElo + winElo;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Statistic.ELO.setForPlayer(ID2, newElo);
        if (game != null) {
            StatChangelog winChangelog = new StatChangelog(ID2, Statistic.WIN, 1);
            game.storeChangelog(winChangelog);
        }
        Statistic.WIN.setForPlayer(ID2, Statistic.WIN.getForPlayer(ID2) + 1.0);
        Player.updateWS(ID2, game);
        Player.fix(ID2, g2);
        ArrayList<Integer> winningvalues = new ArrayList<Integer>();
        winningvalues.add(oldElo);
        winningvalues.add(newElo);
        return winningvalues;
    }

    public static List<Integer> unwin(String ID2, Guild g2) {
        int oldElo;
        int newElo = oldElo = (int)Statistic.ELO.getForPlayer(ID2);
        try {
            for (Rank rank : Rank.getRanks()) {
                int startingElo = rank.getStartingElo();
                int endingElo = rank.getEndingElo();
                int winElo = rank.getWinElo();
                if (oldElo < startingElo || oldElo > endingElo) continue;
                newElo = oldElo - winElo;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Statistic.ELO.setForPlayer(ID2, newElo);
        Player.fix(ID2, g2);
        ArrayList<Integer> winningvalues = new ArrayList<Integer>();
        winningvalues.add(oldElo);
        winningvalues.add(newElo);
        return winningvalues;
    }

    public static Integer getPlacement(String id, Statistic stat) {
        HashMap<String, Double> unsortedMap = new HashMap<String, Double>();
        if (stat == null) {
            return -1;
        }
        for (String ID2 : players.keySet()) {
            unsortedMap.put(ID2, stat.getForPlayer(ID2));
        }
        Map<String, Double> sortedMap = Utils.sortByValue(unsortedMap);
        return Player.getIndexForKey(sortedMap, id) + 1;
    }

    public static int getIndexForKey(Map<String, Double> map, String keyToFind) {
        int index = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entry.getKey().equals(keyToFind)) {
                return index;
            }
            ++index;
        }
        return -2;
    }

    /**
     * Asynchronously load player data and put in cache.
     */
    public static void loadPlayerAsync(String id, Runnable callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File("RBW/players/" + id + ".yml");
                PlayerStoring storing = new PlayerStoring(id, file);
                synchronized (playerLock) {
                    players.put(id, storing);
                    idByName.put(storing.getConfig().getString("name", "unknownnnn"), id);
                }
                if (callback != null) {
                    // Run callback on main thread
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(Player.class), callback);
                }
            }
        }.runTaskAsynchronously(JavaPlugin.getProvidingPlugin(Player.class));
    }

    /**
     * Asynchronously save player data and remove from cache.
     */
    public static void saveAndRemovePlayerAsync(String id, Runnable callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerStoring storing;
                synchronized (playerLock) {
                    storing = players.remove(id);
                }
                if (storing != null) {
                    try {
                        storing.getConfig().save(storing.getF());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (callback != null) {
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(Player.class), callback);
                }
            }
        }.runTaskAsynchronously(JavaPlugin.getProvidingPlugin(Player.class));
    }

    /**
     * Asynchronously save all cached player data.
     */
    public static void saveAllPlayersAsync(Runnable callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                synchronized (playerLock) {
                    for (PlayerStoring storing : players.values()) {
                        try {
                            storing.getConfig().save(storing.getF());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (callback != null) {
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(Player.class), callback);
                }
            }
        }.runTaskAsynchronously(JavaPlugin.getProvidingPlugin(Player.class));
    }

    /**
     * Start periodic async save of all cached players every 60 seconds.
     */
    public static void startPeriodicAsyncSave() {
        if (periodicSaveTimer != null) return;
        periodicSaveTimer = new Timer();
        periodicSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                saveAllPlayersAsync(null);
            }
        }, 60000L, 60000L);
    }

    /**
     * Stop periodic async save.
     */
    public static void stopPeriodicAsyncSave() {
        if (periodicSaveTimer != null) {
            periodicSaveTimer.cancel();
            periodicSaveTimer = null;
        }
    }
}

