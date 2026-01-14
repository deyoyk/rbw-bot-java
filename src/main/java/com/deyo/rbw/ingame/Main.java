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
import com.deyo.rbw.License;
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
        
        Config.loadConfig();
        serverIp = Config.getValue("server-ip");
        if (serverIp == null || serverIp.trim().isEmpty()) {
            serverIp = "server-ip not set on config.yml";
        }

        String licenseKey = Config.getValue("license-key");
        
        if (licenseKey == null || licenseKey.trim().isEmpty()) {
            getLogger().warning("[License] License key not found in config.yml");
            getLogger().info("[License] Adding license-key field to config.yml...");
            
            try {
                Config.getConfig().set("license-key", "YOUR_LICENSE_KEY_HERE");
                Config.getConfig().save(new java.io.File("RBW/config.yml"));
                getLogger().info("[License] ✓ Added license-key field to config.yml");
                getLogger().severe("[License] ✗ CRITICAL ERROR: License key is required");
                getLogger().severe("[License] ✗ Please edit RBW/config.yml and add your license key:");
                getLogger().severe("[License] ✗   license-key: YOUR_LICENSE_KEY_HERE");
                getLogger().severe("[License] ✗ Plugin cannot start without a valid license key");
                getLogger().severe("[License] ✗ Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            } catch (Exception e) {
                getLogger().severe("[License] ✗ Failed to add license-key to config.yml: " + e.getMessage());
                getLogger().severe("[License] ✗ Please manually add 'license-key: YOUR_LICENSE_KEY_HERE' to RBW/config.yml");
                getLogger().severe("[License] ✗ Plugin cannot start without a valid license key");
                getLogger().severe("[License] ✗ Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        
        licenseKey = licenseKey.trim();
        getLogger().info("[License] Starting license verification process...");
        getLogger().info("[License] License key detected in configuration file");
        
        try {
            getLogger().info("[License] Connecting to license server for validation...");
            License.verifyKey(licenseKey);
            
            if (!com.deyo.rbw.Main.valid) {
                getLogger().severe("[License] ✗ CRITICAL ERROR: License verification returned invalid status");
                getLogger().severe("[License] ✗ The license key was rejected by the server");
                getLogger().severe("[License] ✗ Plugin cannot start without valid license verification");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            
            com.deyo.rbw.Main.setupHeartbeatScheduler(licenseKey);
            
            getLogger().info("[License] ✓ License verification completed successfully");
            getLogger().info("[License] ✓ License is valid and active");
            getLogger().info("[License] ✓ Plugin startup authorized");
            getLogger().info("[License] ✓ Heartbeat scheduler initialized (15 minute intervals)");
        } catch (java.net.UnknownHostException e) {
            getLogger().severe("[License] ✗ CRITICAL ERROR: Cannot reach license server");
            getLogger().severe("[License] ✗ Network connectivity issue detected");
            getLogger().severe("[License] ✗ Please check your internet connection and firewall settings");
            getLogger().severe("[License] ✗ Error details: " + e.getMessage());
            getLogger().severe("[License] ✗ Plugin cannot start without valid license verification");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } catch (java.net.SocketTimeoutException e) {
            getLogger().severe("[License] ✗ CRITICAL ERROR: License server connection timeout");
            getLogger().severe("[License] ✗ Server did not respond within the expected time frame");
            getLogger().severe("[License] ✗ This may indicate server overload or network issues");
            getLogger().severe("[License] ✗ Error details: " + e.getMessage());
            getLogger().severe("[License] ✗ Plugin cannot start without valid license verification");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } catch (java.io.IOException e) {
            getLogger().severe("[License] ✗ CRITICAL ERROR: Failed to communicate with license server");
            getLogger().severe("[License] ✗ I/O error occurred during license verification");
            getLogger().severe("[License] ✗ Error details: " + e.getMessage());
            getLogger().severe("[License] ✗ Plugin cannot start without valid license verification");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } catch (Exception e) {
            getLogger().severe("[License] ✗ CRITICAL ERROR: License verification failed");
            getLogger().severe("[License] ✗ The provided license key could not be validated");
            getLogger().severe("[License] ✗ Possible reasons:");
            getLogger().severe("[License]   - Invalid or expired license key");
            getLogger().severe("[License]   - License key has reached its usage limit");
            getLogger().severe("[License]   - License key has been suspended or revoked");
            getLogger().severe("[License]   - Hardware identifier mismatch");
            getLogger().severe("[License] ✗ Error details: " + e.getMessage());
            if (e.getCause() != null) {
                getLogger().severe("[License] ✗ Root cause: " + e.getCause().getMessage());
            }
            getLogger().severe("[License] ✗ Plugin cannot start without valid license verification");
            getLogger().severe("[License] ✗ Please contact support if you believe this is an error");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        com.deyo.rbw.Main.main(new String[0]);
        this.getCommand("link").setExecutor((CommandExecutor)new com.deyo.rbw.ingame.commands.LinkCommand());
        this.getCommand("guild").setExecutor((CommandExecutor)new com.deyo.rbw.ingame.commands.GuildCommand());
        new RBWPlaceholders().register();
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
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


