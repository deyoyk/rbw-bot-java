/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.events;

import java.io.File;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.ingame.sync.RoleSync;
import com.deyo.rbw.ingame.sync.TagSync;

public class JoinEvent
extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (new File("RBW/players/" + event.getMember().getId() + ".yml").exists()) {
            Player.fix(event.getMember().getId(), event.getGuild());
            BetterEmbed embed = new BetterEmbed("default", "Welcome Back", "", "I've noticed it's not your first time in this server\nI corrected your stats and updated your nickname - you don't need to register again!", "");
            if (RBW.alertsChannel != null) {
                ((MessageCreateAction)RBW.alertsChannel.sendMessage(event.getMember().getAsMention()).setEmbeds(embed.build())).queue();
            }
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        org.bukkit.entity.Player player;
        super.onGuildMemberRoleAdd(event);
        if (Player.isPlayer(event.getMember().getId()) && (player = Bukkit.getPlayer((String)Player.getName(event.getMember().getId()))) != null) {
            TagSync.run(player);
            RoleSync.run(player);
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        org.bukkit.entity.Player player;
        super.onGuildMemberRoleRemove(event);
        if (Player.isPlayer(event.getMember().getId()) && (player = Bukkit.getPlayer((String)Player.getName(event.getMember().getId()))) != null) {
            TagSync.run(player);
            RoleSync.run(player);
        }
    }
}

