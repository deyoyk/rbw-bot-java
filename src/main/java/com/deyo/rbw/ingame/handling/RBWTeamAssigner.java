/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.andrei1058.bedwars.api.arena.IArena
 *  com.andrei1058.bedwars.api.arena.team.ITeam
 *  com.andrei1058.bedwars.arena.team.TeamAssigner
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.ingame.handling;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.arena.team.TeamAssigner;
import com.deyo.rbw.classes.GameMap;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;

import java.util.HashMap;
import org.bukkit.Bukkit;

public class RBWTeamAssigner
extends TeamAssigner {
    public static HashMap<IArena, Game> onGoingGames = new HashMap();

    public void assignTeams(IArena arena) {
        org.bukkit.entity.Player p;
        Game game = onGoingGames.get(arena);
        if (game == null) {
            return;
        }
        GameMap map = GameMap.getFromName(arena.getWorldName());
        if (map == null) {
            System.out.println("Couldn't find map #33");
            return;
        }
        ITeam team1 = null;
        ITeam team2 = null;
        for (ITeam team : arena.getTeams()) {
            if (team.getColor() == map.getTeam1()) {
                team1 = team;
            }
            if (team.getColor() != map.getTeam2()) continue;
            team2 = team;
        }
        if (team1 == null || team2 == null) {
            return;
        }
        for (String id : game.getTeam(1)) {
            p = Bukkit.getPlayer((String)Player.getName(id));
            if (p == null || !arena.getPlayers().contains(p)) {
                System.out.println("Player was not in arena but was in team pool! wtf?");
                continue;
            }
            team1.addPlayers(new org.bukkit.entity.Player[]{p});
        }
        for (String id : game.getTeam(2)) {
            p = Bukkit.getPlayer((String)Player.getName(id));
            if (p == null || !arena.getPlayers().contains(p)) {
                System.out.println("Player was not in arena but was in team pool! wtf?");
                continue;
            }
            team2.addPlayers(new org.bukkit.entity.Player[]{p});
        }
    }
}

