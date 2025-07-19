/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.andrei1058.bedwars.api.arena.IArena
 *  com.andrei1058.bedwars.arena.Arena
 *  net.luckperms.api.LuckPerms
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.deyo.rbw.ingame;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.classes.GameMap;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.ingame.RBWPlaceholders;
import com.deyo.rbw.ingame.commands.LinkCommand;
import com.deyo.rbw.ingame.handling.RBWArenaListeners;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin {
    public static Main instance;
    public static String serverIp;
    public static LuckPerms luckPermsAPI;

    public void onEnable() {
        super.onEnable();
        instance = this;
        serverIp = "grbw.fun";
        com.deyo.rbw.Main.main(new String[0]);
        this.getCommand("link").setExecutor((CommandExecutor)new com.deyo.rbw.ingame.commands.LinkCommand());
        this.getCommand("guild").setExecutor((CommandExecutor)new com.deyo.rbw.ingame.commands.GuildCommand());
        new RBWPlaceholders().register();
        RegisteredServiceProvider provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsAPI = (LuckPerms)provider.getProvider();
        }
        Bukkit.getPluginManager().registerEvents((Listener)new RBWArenaListeners(), (Plugin)this);
        
        Bukkit.getScheduler().runTaskLater((Plugin)instance, () -> {
            for (IArena arena : Arena.getArenas()) {
                if (arena.getGroup().equals(Config.getValue("group"))) {
                    new GameMap(arena, Queue.QueueMaps.Quads);
                }
                if (!arena.getGroup().equals(Config.getValue("group-2v2"))) continue;
                new GameMap(arena, Queue.QueueMaps.Doubles);
            }
        }, 400L);
    }

    public void onDisable() {
        super.onDisable();
        com.deyo.rbw.Main.onDisable();
    }
}


