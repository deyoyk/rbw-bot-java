/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.andrei1058.bedwars.api.arena.GameState
 *  com.andrei1058.bedwars.api.arena.IArena
 *  com.andrei1058.bedwars.api.arena.team.ITeamAssigner
 *  com.andrei1058.bedwars.api.arena.team.TeamColor
 *  com.andrei1058.bedwars.arena.Arena
 */
package com.deyo.rbw.classes;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeamAssigner;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.arena.Arena;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.ingame.handling.RBWTeamAssigner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class GameMap {
    public static HashMap<String, GameMap> gameMaps = new HashMap();
    private String displayName;
    private int height;
    private String worldName;
    private Queue.QueueMaps mapType;

    public GameMap(IArena arena, Queue.QueueMaps type) {
        arena.setTeamAssigner((ITeamAssigner)new RBWTeamAssigner());
        if (gameMaps.containsKey(arena.getWorldName())) {
            return;
        }
        this.displayName = arena.getDisplayName();
        this.mapType = type;
        this.height = arena.getConfig().getInt("max-build-y");
        this.worldName = arena.getWorldName();
        gameMaps.put(this.worldName, this);
    }

    public Queue.QueueMaps getMapType() {
        return this.mapType;
    }

    public static Collection<GameMap> getMaps() {
        return gameMaps.values();
    }

    public IArena getArena() {
        return Arena.getArenaByIdentifier((String)this.worldName);
    }

    public static GameMap getFromName(String worldName) {
        for (GameMap gameMap : gameMaps.values()) {
            if (!gameMap.worldName.equals(worldName)) continue;
            return gameMap;
        }
        return null;
    }

    public static ArrayList<GameMap> getFreeArenas(Queue.QueueMaps mapType) {
        ArrayList<GameMap> arenas = new ArrayList<GameMap>();
        for (GameMap gameMap : gameMaps.values()) {
            IArena arena;
            if (gameMap.mapType != mapType || (arena = gameMap.getArena()) == null || arena.getStatus() != GameState.waiting || !arena.getPlayers().isEmpty()) continue;
            arenas.add(gameMap);
        }
        return arenas;
    }

    public static GameMap getRandomMap(Queue.QueueMaps mapType) {
        if (gameMaps.isEmpty()) {
            return null;
        }
        ArrayList<GameMap> freeArenas = GameMap.getFreeArenas(mapType);
        Collections.shuffle(freeArenas);
        Iterator<GameMap> iterator2 = freeArenas.iterator();
        if (iterator2.hasNext()) {
            GameMap map = iterator2.next();
            return map;
        }
        return null;
    }

    public TeamColor getTeam1() {
        return TeamColor.RED;
    }

    public TeamColor getTeam2() {
        return this.mapType == Queue.QueueMaps.Quads ? TeamColor.GREEN : TeamColor.AQUA;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getHeight() {
        return this.height;
    }
}

