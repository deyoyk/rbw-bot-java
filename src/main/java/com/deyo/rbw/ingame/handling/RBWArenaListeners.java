/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.andrei1058.bedwars.api.arena.GameState
 *  com.andrei1058.bedwars.api.arena.IArena
 *  com.andrei1058.bedwars.api.events.gameplay.GameEndEvent
 *  com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent
 *  com.andrei1058.bedwars.api.events.player.PlayerKillEvent
 *  com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent
 *  com.andrei1058.bedwars.api.events.server.ArenaEnabdeyovent
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.ingame.handling;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.api.events.server.ArenaEnableEvent;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.GameMap;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.Statistic;
import com.deyo.rbw.ingame.Main;
import com.deyo.rbw.ingame.handling.ArenaUtils;
import com.deyo.rbw.ingame.handling.RBWTeamAssigner;
import com.deyo.rbw.ingame.sync.RoleSync;
import com.deyo.rbw.ingame.sync.TagSync;

import java.util.ArrayList;
import java.util.Collections;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class RBWArenaListeners
implements Listener {
    public static ArrayList<IArena> leaving = new ArrayList();
    public static String name = "\u00a7b\u00a7lPlay Again";
    public static String lore = "\u00a77Click to play again and get moved to queue";
    public static ArrayList<String> cooldown = new ArrayList();

    @EventHandler
    public void onJoinRoleTagSync(PlayerJoinEvent e) {
        org.bukkit.entity.Player bukkitPlayer = e.getPlayer();
        RoleSync.run(bukkitPlayer);
        TagSync.run(bukkitPlayer);
    }

    @EventHandler
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        if (leaving.contains(e.getArena())) {
            return;
        }
        if (e.getArena().getStatus().equals((Object)com.andrei1058.bedwars.api.arena.GameState.playing) || e.getArena().getStatus().equals((Object)com.andrei1058.bedwars.api.arena.GameState.restarting)) {
            return;
        }
        Game g2 = RBWTeamAssigner.onGoingGames.get(e.getArena());
        if (g2 != null) {
            System.out.println(e.getPlayer().getName() + " left the game while starting ");
            ArenaUtils.kickAll(e.getArena());
            g2.retry(GameMap.getFromName(e.getArena().getWorldName()), RBW.mainGuild);
        }
    }

    @EventHandler
    public void onArenaEnable(ArenaEnableEvent e) {
        if (e.getArena().getGroup().equals(Config.getValue("group"))) {
            new GameMap(e.getArena(), Queue.QueueMaps.Quads);
        }
        if (e.getArena().getGroup().equals(Config.getValue("group-2v2"))) {
            new GameMap(e.getArena(), Queue.QueueMaps.Doubles);
        }
    }

    @EventHandler
    public void onKillDeathEvent(PlayerKillEvent e) {
        Game g2 = RBWTeamAssigner.onGoingGames.get(e.getArena());
        if (g2 != null) {
            String ID2;
            if (e.getKiller() != null) {
                ID2 = Player.getIdFromIGN(e.getKiller().getName());
                g2.addKill(ID2);
            }
            if (e.getVictim() != null) {
                ID2 = Player.getIdFromIGN(e.getVictim().getName());
                g2.addDeath(ID2);
            }
        }
    }

    @EventHandler
    public void onBed(PlayerBedBreakEvent e) {
        Game g2 = RBWTeamAssigner.onGoingGames.get(e.getArena());
        if (g2 != null) {
            String ID2 = Player.getIdFromIGN(e.getPlayer().getName());
            g2.addBed(ID2);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.PAPER && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
            String ID2 = Player.getIdFromIGN(e.getPlayer().getName());
            if (ID2 == null) {
                e.getPlayer().sendMessage("\u00a7cCouldn't find you in the discord.");
                return;
            }
            if (cooldown.contains(e.getPlayer().getName())) {
                e.getPlayer().sendMessage("\u00a7cYou're in cooldown.");
                return;
            }
            Member m3 = RBW.mainGuild.getMemberById(ID2);
            if (m3 == null) {
                e.getPlayer().sendMessage("\u00a7cCouldn't find you in the discord.");
                return;
            }
            Queue q = null;
            for (Game playerGame : Game.getPlayerGames(ID2, 1)) {
                q = Queue.getQueueFromID(playerGame.getQueueID());
            }
            if (q == null) {
                e.getPlayer().sendMessage("\u00a7cCouldn't find the queue you were in.");
                return;
            }
            if (m3.getVoiceState() == null || m3.getVoiceState().getChannel() == null) {
                e.getPlayer().sendMessage("\u00a7cYou are not connected to a voice channel.");
                return;
            }
            VoiceChannel vc = RBW.mainGuild.getVoiceChannelById(q.getId());
            if (vc == null) {
                e.getPlayer().sendMessage("\u00a7cCouldn't find the queue you were in.");
                return;
            }
            try {
                cooldown.add(e.getPlayer().getName());
                e.getPlayer().sendMessage("\u00a7eTrying to warp you!");
                RBW.mainGuild.moveVoiceMember(m3, vc).queue(a -> {
                    e.getPlayer().sendMessage("\u00a7aSuccessfully moved you to queue!");
                    cooldown.remove(e.getPlayer().getName());
                });
            }
            catch (Exception ex) {
                e.getPlayer().sendMessage("\u00a7cAn error occured while trying to move you.");
                return;
            }
        }
    }

    @EventHandler
    public void onGameEnd(GameEndEvent e) {
        Game g2 = RBWTeamAssigner.onGoingGames.get(e.getArena());
        if (g2 != null) {
            if (g2.getState() == GameState.VOIDED) {
                return;
            }
            GameMap map = GameMap.getFromName(e.getArena().getWorldName());
            if (map == null) {
                System.out.println("Couldn't find map #34");
                return;
            }
            int winningteam = e.getTeamWinner().getColor() == map.getTeam1() ? 1 : 2;
            ArrayList<String> mvps = new ArrayList<String>();
            int mvpKills = 0;
            for (int t : new int[]{1, 2}) {
                for (String ID2 : g2.getTeam(t)) {
                    int kills = g2.getKills(ID2);
                    if (kills > mvpKills) {
                        mvpKills = kills;
                        mvps.clear();
                        mvps.add(ID2);
                        continue;
                    }
                    if (kills != mvpKills) continue;
                    mvps.add(ID2);
                }
            }
            System.out.println("Scored game " + g2.gamenumber);
            System.out.println("Winning Team: " + winningteam);
            System.out.println("MVPs: " + mvps);
            for (org.bukkit.entity.Player player : e.getArena().getWorld().getPlayers()) {
                ItemStack itemStack = new ItemStack(Material.PAPER, 1);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName(name);
                meta.setLore(Collections.singletonList(lore));
                itemStack.setItemMeta(meta);
                player.getInventory().addItem(new ItemStack[]{itemStack});
            }
            g2.score("n", winningteam, mvps);
            for (org.bukkit.entity.Player player : e.getArena().getWorld().getPlayers()) {
                String ID3 = Player.getIdFromIGN(player.getName());
                if (ID3 == null) continue;
                player.sendMessage("\u00a7b\u00a7lYour new elo of \u00a7e\u00a7l" + (int)Statistic.ELO.getForPlayer(ID3) + " \u00a7b\u00a7lhas put you \u00a7e\u00a7l#" + Player.getPlacement(ID3, Statistic.ELO));
            }
            RBWTeamAssigner.onGoingGames.remove(e.getArena());
        }
    }
}
