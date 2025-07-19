/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.ingame.sync;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.ingame.Main;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Member;
public class TagSync {
    public static HashMap<Role, String> roles = new HashMap<>();
    private static final Map<String, String> playerDiscordIdCache = new HashMap<>();
    private static final Map<String, Set<String>> playerCurrentTags = new HashMap<>();

    public static void load() {
        for (String ID2 : Config.getConfig().getConfigurationSection("tagsync").getKeys(true)) {
            String tagName = Config.getConfig().getString("tagsync." + ID2);
            Role role = RBW.mainGuild.getRoleById(ID2);
            if (role == null) continue;
            roles.put(role, tagName);
            System.out.println("Loaded tagsync " + role.getName() + " (" + tagName + ")");
        }
    }

    public static void run(@NotNull org.bukkit.entity.Player player) {
        final String playerName = player.getName();
        String discordId = playerDiscordIdCache.get(playerName);
        if (discordId == null) {
            discordId = Player.getIdFromIGN(playerName);
            if (discordId != null) {
                playerDiscordIdCache.put(playerName, discordId);
            }
        }
        if (discordId == null || RBW.mainGuild == null || roles.isEmpty()) {
            return;
        }
        final String finalDiscordId = discordId;
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            try {
                Member member = RBW.mainGuild.getMemberById(finalDiscordId);
                if (member == null) {
                    return;
                }
                Set<String> currentTags = playerCurrentTags.getOrDefault(playerName, new HashSet<>());
                Set<String> tagsToAdd = new HashSet<>();
                Set<String> tagsToRemove = new HashSet<>();
                for (Map.Entry<Role, String> entry : roles.entrySet()) {
                    Role role = entry.getKey();
                    String tag = entry.getValue();
                    boolean hasRole = member.getRoles().contains(role);
                    boolean hasPermission = currentTags.contains(tag);
                    if (hasRole && !hasPermission) {
                        tagsToAdd.add(tag);
                    } else if (!hasRole && hasPermission) {
                        tagsToRemove.add(tag);
                    }
                }
                if (!tagsToAdd.isEmpty() || !tagsToRemove.isEmpty()) {
                    // Use LuckPerms API to update permissions asynchronously
                    Main.luckPermsAPI.getUserManager().loadUser(player.getUniqueId()).thenAcceptAsync(user -> {
                        if (user == null) return;
                        boolean changed = false;
                        for (String tag : tagsToAdd) {
                            user.data().add(net.luckperms.api.node.types.PermissionNode.builder("celestialtags.tag." + tag).build());
                            currentTags.add(tag);
                            changed = true;
                        }
                        for (String tag : tagsToRemove) {
                            user.data().remove(net.luckperms.api.node.types.PermissionNode.builder("celestialtags.tag." + tag).build());
                            currentTags.remove(tag);
                            changed = true;
                        }
                        if (changed) {
                            Main.luckPermsAPI.getUserManager().saveUser(user);
                            playerCurrentTags.put(playerName, currentTags);
                        }
                    });
                }
            } catch (Exception ex) {
            }
        });
    }
}

