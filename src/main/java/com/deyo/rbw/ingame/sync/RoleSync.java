/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.luckperms.api.model.user.User
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.ingame.sync;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.ingame.Main;

public class RoleSync {
    public static HashMap<Role, String> roles = new HashMap<>();
    private static final Map<String, String> playerDiscordIdCache = new HashMap<>();
    private static final Map<String, Set<String>> playerCurrentPermissions = new HashMap<>();

    public static void load() {
        for (String ID2 : Config.getConfig().getConfigurationSection("rolesync").getKeys(true)) {
            String rolename = Config.getConfig().getString("rolesync." + ID2);
            Role role = RBW.mainGuild.getRoleById(ID2);
            if (role == null) continue;
            roles.put(role, rolename);
            System.out.println("Loaded syncrole " + role.getName() + " (" + rolename + ")");
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
        if (discordId == null || RBW.mainGuild == null) {
            return;
        }
        final String finalDiscordId = discordId;
        Main.luckPermsAPI.getUserManager().loadUser(player.getUniqueId()).thenAcceptAsync(user -> {
            if (user == null) {
                return;
            }
            Member member = RBW.mainGuild.getMemberById(finalDiscordId);
            if (member == null) {
                return;
            }
            String primaryGroup = user.getPrimaryGroup();
            if (roles.isEmpty()) {
                return;
            }
            Set<String> currentPermissions = playerCurrentPermissions.getOrDefault(playerName, new HashSet<>());
            Set<String> addPermissions = new HashSet<>();
            Set<String> removePermissions = new HashSet<>();
            for (Map.Entry<Role, String> entry : roles.entrySet()) {
                final String entryValue = entry.getValue();
                final boolean hasRole = member.getRoles().contains(entry.getKey());
                final boolean isPrimaryGroup = entryValue.equalsIgnoreCase(primaryGroup);
                if (hasRole && !isPrimaryGroup) {
                    if (!currentPermissions.contains(entryValue)) {
                        addPermissions.add(entryValue);
                    }
                } else if (!hasRole && isPrimaryGroup) {
                    if (currentPermissions.contains(entryValue)) {
                        removePermissions.add(entryValue);
                    }
                }
            }
            if (!addPermissions.isEmpty() || !removePermissions.isEmpty()) {
                boolean changed = false;
                for (String perm : addPermissions) {
                    user.data().add(net.luckperms.api.node.types.InheritanceNode.builder(perm).build());
                    currentPermissions.add(perm);
                    changed = true;
                }
                for (String perm : removePermissions) {
                    user.data().remove(net.luckperms.api.node.types.InheritanceNode.builder(perm).build());
                    currentPermissions.remove(perm);
                    changed = true;
                }
                if (changed) {
                    Main.luckPermsAPI.getUserManager().saveUser(user);
                    playerCurrentPermissions.put(playerName, currentPermissions);
                }
            }
        });
    }
}

