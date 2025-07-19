/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.andrei1058.bedwars.api.arena.GameState
 *  com.andrei1058.bedwars.api.arena.IArena
 *  com.andrei1058.bedwars.arena.Arena
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.ingame.handling;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.deyo.rbw.ingame.Main;
import com.deyo.rbw.ingame.handling.RBWArenaListeners;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ArenaUtils {
    public static void kickAll(IArena arena) {
        Bukkit.getScheduler().runTask((Plugin)Main.instance, () -> {
            RBWArenaListeners.leaving.add(arena);
            for (Player player : new ArrayList<Player>(arena.getPlayers())) {
                arena.removePlayer(player, false);
            }
            RBWArenaListeners.leaving.remove(arena);
        });
    }

    public static void warpAll(Collection<Player> players, IArena arena) {
        for (Player player : players) {
            Bukkit.getScheduler().runTask((Plugin)Main.instance, () -> {
                IArena currentArena = Arena.getArenaByPlayer(player);
                if (currentArena != null) {
                    if (currentArena.isPlayer(player)) {
                        currentArena.removePlayer(player, false);
                    }
                    if (currentArena.isSpectator(player)) {
                        currentArena.removeSpectator(player, false);
                    }
                }
            });
        }
        Bukkit.getScheduler().runTaskLater((Plugin)Main.instance, () -> {
            for (Player player : players) {
                arena.addPlayer(player, false);
            }
            arena.changeStatus(GameState.starting);
            if (arena.getStartingTask().getCountdown() > 5) {
                arena.getStartingTask().setCountdown(5);
            }
        }, 20L);
    }
}

