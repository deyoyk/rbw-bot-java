/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.andrei1058.bedwars.BedWars
 *  com.andrei1058.bedwars.api.arena.GameState
 *  com.andrei1058.bedwars.api.arena.IArena
 *  com.andrei1058.bedwars.arena.Arena
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.deyo.rbw.classes.game;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.GameMap;
import com.deyo.rbw.classes.Modifiers;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.StatChangelog;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.GameStoring;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;
import com.deyo.rbw.ingame.handling.ArenaUtils;
import com.deyo.rbw.ingame.handling.RBWTeamAssigner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Game {
    public static ArrayList<Game> games = new ArrayList<>();
    public final int gamenumber;
    private transient final GameStoring storing;
    private transient final YamlConfiguration config;
    private byte retries = 0;
    byte maxRetries = (byte)4;

    public GameStoring getStoring() {
        return this.storing;
    }

    public Game(int gamenumber, Queue q) {
        this.gamenumber = gamenumber;
        this.storing = new GameStoring(gamenumber);
        this.config = this.storing.getConfig();
        this.setMapType(q.getMapUsed());
        this.setCasual(q.isCasual());
        this.setMode(q.getPicking());
        this.setQueueID(q.getId());
        games.add(this);
    }

    public Game(int gamenumber, File file) {
        this.gamenumber = gamenumber;
        this.storing = new GameStoring(gamenumber, file);
        this.config = this.storing.getConfig();
        games.add(this);
    }

    public static Game getFromVC(String id) {
        for (Game game : games) {
            for (int i = 1; i < 3; ++i) {
                if (!game.getVoiceChannel(i).equalsIgnoreCase(id)) continue;
                return game;
            }
        }
        return null;
    }

    public Queue.QueueMaps getMapType() {
        Queue.QueueMaps map = null;
        try {
            map = Queue.QueueMaps.valueOf(this.config.getString("map-type"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        return map;
    }

    public void setMapType(Queue.QueueMaps mapType) {
        this.config.set("map-type", mapType.name());
    }

    public void setCasual(boolean casual) {
        this.config.set("casual", casual);
    }

    public String getQueueID() {
        return this.config.getString("queue-id");
    }

    public void setQueueID(String ID2) {
        this.config.set("queue-id", ID2);
    }

    public boolean isCasual() {
        return this.config.getBoolean("casual");
    }

    public void setMode(String mode) {
        this.config.set("mode", mode);
    }

    public String getMode() {
        return this.config.getString("mode");
    }

    public long getStartedAt() {
        return this.config.getLong("startedat");
    }

    public void clearChangelogs() {
        this.config.set("changelogs", null);
    }

    public void storeChangelog(StatChangelog changelog) {
        ArrayList<StatChangelog> changelogs = this.getChangelogs(changelog.getID());
        changelogs.add(changelog);
        ArrayList<String> formatted = new ArrayList<String>();
        for (StatChangelog statChangelog : changelogs) {
            formatted.add(statChangelog.getStatistic().name() + "::" + statChangelog.getChange());
        }
        this.config.set("changelogs." + changelog.getID(), formatted);
    }

    public ArrayList<StatChangelog> getChangelogs(String ID2) {
        ArrayList<StatChangelog> changelogs = new ArrayList<StatChangelog>();
        if (!this.config.isSet("changelogs." + ID2)) {
            return changelogs;
        }
        for (String s2 : this.config.getStringList("changelogs." + ID2)) {
            int change;
            Statistic statistic;
            String[] a = s2.split("::");
            try {
                statistic = Statistic.valueOf(a[0]);
            }
            catch (Exception ex) {
                continue;
            }
            try {
                change = Integer.parseInt(a[1]);
            }
            catch (Exception ex) {
                continue;
            }
            changelogs.add(new StatChangelog(ID2, statistic, change));
        }
        return changelogs;
    }

    public void setStartedAt() {
        this.config.set("startedat", System.currentTimeMillis());
    }

    public int getWinnerteam() {
        return this.config.getInt("winnerteam");
    }

    public void setWinnerteam(int winnerteam) {
        this.config.set("winnerteam", winnerteam);
    }

    public String getCaptain(int number) {
        return this.config.getString("captain-" + number);
    }

    public void setCaptain(int number, String ID2) {
        this.config.set("captain-" + number, ID2);
    }

    public String getCurrentCaptain() {
        return this.config.getString("currentcaptain");
    }

    public void setCurrentCaptain(String ID2) {
        this.config.set("currentcaptain", ID2);
    }

    public List<String> getGameMembers() {
        return this.config.getStringList("gamemembers");
    }

    public void setGameMembers(List<String> gm) {
        this.config.set("gamemembers", gm);
    }

    public List<String> getTeam(int num) {
        return this.config.getStringList("team-" + num);
    }

    public void setTeam(int num, List<String> players) {
        this.config.set("team-" + num, players);
    }

    public com.deyo.rbw.commands.types.GameState getState() {
        return com.deyo.rbw.commands.types.GameState.valueOf(this.config.getString("gamestate"));
    }

    public void addKill(String ID2) {
        String a = "kills." + ID2;
        this.config.set(a, this.config.getInt(a, 0) + 1);
    }

    public int getKills(String ID2) {
        String a = "kills." + ID2;
        return this.config.getInt(a, 0);
    }

    public void addDeath(String ID2) {
        String a = "deaths." + ID2;
        this.config.set(a, this.config.getInt(a, 0) + 1);
    }

    public int getDeaths(String ID2) {
        String a = "deaths." + ID2;
        return this.config.getInt(a, 0);
    }

    public void addBed(String ID2) {
        String a = "beds." + ID2;
        this.config.set(a, this.config.getInt(a, 0) + 1);
    }

    public int getBed(String ID2) {
        String a = "beds." + ID2;
        return this.config.getInt(a, 0);
    }

    public void setState(com.deyo.rbw.commands.types.GameState e) {
        this.config.set("gamestate", e.name());
    }

    public String getMap() {
        IArena arena = BedWars.getAPI().getArenaUtil().getArenaByName(this.config.getString("map"));
        return arena == null ? this.config.getString("map") : arena.getDisplayName();
    }

    public IArena getArena() {
        return Arena.getArenaByName((String)this.config.getString("map", ""));
    }

    public void setMap(String worldname) {
        this.config.set("map", worldname);
    }

    public String getGameChannel() {
        return this.config.getString("gamechannel");
    }

    public void setGameChannel(String ID2) {
        this.config.set("gamechannel", ID2);
    }

    public String getVoiceChannel(int num) {
        return this.config.getString("vc-" + num);
    }

    public void setVoiceChannel(int num, String ID2) {
        this.config.set("vc-" + num, ID2);
    }

    public List<String> getMvps() {
        return this.config.getStringList("mvps");
    }

    public void setMvps(ArrayList<String> IDs) {
        this.config.set("mvps", IDs);
    }

    public String getScoredBy() {
        return this.config.getString("scoredby");
    }

    public void setScoredBy(String scoredBy) {
        this.config.set("scoredby", scoredBy);
    }

    public int getCurrentPick() {
        return this.config.getInt("currentpick", 0);
    }

    public void setCurrentPick(int pick) {
        this.config.set("currentpick", pick);
    }

    public byte pick(Member captain, Member player, Guild g2) {
        String ID2;
        List<String> players = this.getGameMembers();
        if (!players.contains(ID2 = player.getId())) {
            return 0;
        }
        List<String> team1 = this.getTeam(1);
        List<String> team2 = this.getTeam(2);
        players.remove(ID2);
        if (players.size() >= 2) {
            this.setGameMembers(players);
            if (captain.getId().equals(this.getCaptain(1))) {
                team1.add(ID2);
                this.setTeam(1, team1);
            } else {
                team2.add(ID2);
                this.setTeam(2, team2);
            }
            int pick = this.getCurrentPick() + 1;
            this.setCurrentPick(pick);
            int currentCaptain = Config.getConfig().getIntegerList("pick-intervals").get(pick);
            this.setCurrentCaptain(this.getCaptain(currentCaptain));
            Object remaining = "";
            Object t1 = "";
            Object t2 = "";
            for (String s2 : players) {
                remaining = (String)remaining + "\u2022 <@!" + s2 + ">\n";
            }
            for (String s2 : team1) {
                t1 = (String)t1 + "\u2022 <@!" + s2 + ">\n";
            }
            for (String s2 : team2) {
                t2 = (String)t2 + "\u2022 <@!" + s2 + ">\n";
            }
            BetterEmbed embed = new BetterEmbed("default", "Game`#" + this.gamenumber + "` picking", "", "<@!" + this.getCurrentCaptain() + "> pick a player using `=pick <mention>`", "");
            embed.addField("Team 1", (String)t1, false);
            embed.addField("Team 2", (String)t2, false);
            embed.addField("Remaining players", (String)remaining, false);
            TextChannel channel = g2.getTextChannelById(this.getGameChannel());
            if (channel != null) {
                channel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            }
        } else {
            GameMap map = GameMap.getRandomMap(this.getMapType());
            if (map == null) {
                return 2;
            }
            if (captain.getId().equals(this.getCaptain(1))) {
                team1.add(ID2);
            } else {
                team2.add(ID2);
            }
            int pick = this.getCurrentPick() + 1;
            this.setCurrentPick(pick);
            int currentCaptain = Config.getConfig().getIntegerList("pick-intervals").get(pick);
            this.setCurrentCaptain(this.getCaptain(currentCaptain));
            if (team1.size() <= team2.size()) {
                team1.add(players.get(0));
            } else {
                team2.add(players.get(0));
            }
            this.setTeam(1, team1);
            this.setTeam(2, team2);
            players.clear();
            this.setGameMembers(players);
            String vc1id = this.getVoiceChannel(1);
            String vc2id = this.getVoiceChannel(2);
            Object t1 = "";
            for (String string : team1) {
                t1 = (String)t1 + "\u2022 " + ((Member)g2.retrieveMemberById(string).complete()).getAsMention() + "\n";
                g2.getVoiceChannelById(vc1id).upsertPermissionOverride((IPermissionHolder)g2.retrieveMemberById(string).complete()).setDenied(Permission.VOICE_CONNECT).queue();
            }
            Object t2 = "";
            for (String s4 : team2) {
                t2 = (String)t2 + "\u2022 " + ((Member)g2.retrieveMemberById(s4).complete()).getAsMention() + "\n";
                g2.getVoiceChannelById(vc2id).upsertPermissionOverride((IPermissionHolder)g2.retrieveMemberById(s4).complete()).setAllowed(Permission.VIEW_CHANNEL).setAllowed(Permission.VOICE_CONNECT).queue();
                if (!g2.getMemberById(s4).getVoiceState().inAudioChannel()) continue;
                g2.moveVoiceMember((Member)g2.retrieveMemberById(s4).complete(), g2.getVoiceChannelById(vc2id)).queue();
            }
            BetterEmbed betterEmbed = new BetterEmbed("default", "Game`#" + this.gamenumber + "` has started", "", "", "");
            betterEmbed.addField("Team 1", (String)t1, false);
            betterEmbed.addField("Team 2", (String)t2, false);
            betterEmbed.addField("Randomly Picked Map", "**" + map.getDisplayName() + "** \u2014 `Height: " + map.getHeight() + "` (" + Utils.formatName(map.getTeam1().name()) + " vs " + Utils.formatName(map.getTeam2().name()) + ")", false);
            g2.getTextChannelById(this.getGameChannel()).sendMessageEmbeds(betterEmbed.build(), new MessageEmbed[0]).queue();
            if (RBW.gamesAnnouncingChannel != null) {
                RBW.gamesAnnouncingChannel.sendMessageEmbeds(betterEmbed.build(), new MessageEmbed[0]).queue();
            }
            Bukkit.getScheduler().runTaskLater((Plugin)com.deyo.rbw.ingame.Main.instance, () -> this.retry(map, g2), 20L);
        }
        return 1;
    }

    public void startGame(List<Member> players, Guild g2, ArrayList<Member> party1, ArrayList<Member> party2) throws IOException {
        this.setStartedAt();
        int playersinteam = players.size() / 2;
        String channelName = Config.getValue("game-channel-names").replaceAll("%number%", String.valueOf(this.gamenumber)).replaceAll("%mode%", playersinteam + "v" + playersinteam);
        String vcNames = Config.getValue("game-vc-names").replaceAll("%number%", String.valueOf(this.gamenumber)).replaceAll("%mode%", playersinteam + "v" + playersinteam);
        Category games = RBW.gameChannelsCategory;
        if (games == null) {
            System.out.println("Text channels category is null.");
            return;
        }
        TextChannel gameChannel = (TextChannel)games.createTextChannel(channelName).complete();
        new Transcript(gameChannel.getId());
        this.setGameChannel(gameChannel.getId());
        Category vcs = RBW.gameVcsCategory;
        if (vcs == null) {
            System.out.println("Voice channels category is null.");
            return;
        }
        VoiceChannel gameVC1 = (VoiceChannel)vcs.createVoiceChannel(vcNames.replaceAll("%team%", "1")).complete();
        this.setVoiceChannel(1, gameVC1.getId());
        VoiceChannel gameVC2 = (VoiceChannel)vcs.createVoiceChannel(vcNames.replaceAll("%team%", "2")).complete();
        this.setVoiceChannel(2, gameVC2.getId());
        StringBuilder mentions = new StringBuilder();
        for (Member member : players) {
            gameChannel.upsertPermissionOverride(member).setAllowed(Permission.VIEW_CHANNEL).queue();
            mentions.append(member.getAsMention());
        }
        BetterEmbed embed = new BetterEmbed("default", "Game `#" + this.gamenumber + "`", "", "", "");
        this.setState(com.deyo.rbw.commands.types.GameState.STARTING);
        if (this.getMode().equals("automatic")) {
            GameMap map = GameMap.getRandomMap(this.getMapType());
            if (map == null) {
                gameChannel.sendMessage("Couldn't assign a map for this game...Wait until some games are done (requeue)").queue();
                return;
            }
            if (party1.size() > 1) {
                Modifiers.Types type;
                ArrayList<Member> modifiedplayers = new ArrayList<Member>(players);
                ArrayList<String> team1 = new ArrayList<String>();
                ArrayList<String> team2 = new ArrayList<String>();
                try {
                    type = Modifiers.Types.valueOf("partyOf" + party1.size());
                }
                catch (IllegalArgumentException ex) {
                    switch (party1.size()) {
                        case 3: {
                            type = Modifiers.Types.partyOf3;
                            break;
                        }
                        case 4: {
                            type = Modifiers.Types.partyOf4;
                            break;
                        }
                        default: {
                            type = Modifiers.Types.partyOf2;
                        }
                    }
                }
                for (Member member : party1) {
                    Modifiers.add(member.getId(), this.gamenumber, type);
                    team1.add(member.getId());
                    modifiedplayers.remove(g2.getMemberById(member.getId()));
                }
                if (party2.size() > 1) {
                    Modifiers.Types type1;
                    try {
                        type1 = Modifiers.Types.valueOf("partyOf" + party2.size());
                    }
                    catch (IllegalArgumentException ex) {
                        switch (party2.size()) {
                            case 3: {
                                type1 = Modifiers.Types.partyOf3;
                                break;
                            }
                            case 4: {
                                type1 = Modifiers.Types.partyOf4;
                                break;
                            }
                            default: {
                                type1 = Modifiers.Types.partyOf2;
                            }
                        }
                    }
                    for (Member member : party2) {
                        Modifiers.add(member.getId(), this.gamenumber, type1);
                        team2.add(member.getId());
                        modifiedplayers.remove(g2.getMemberById(member.getId()));
                    }
                }
                int t1a = team1.size();
                int t2a = team2.size();
                for (Member leftmember : modifiedplayers) {
                    if (t1a < playersinteam) {
                        team1.add(leftmember.getId());
                        ++t1a;
                        continue;
                    }
                    if (t2a < playersinteam) {
                        team2.add(leftmember.getId());
                        ++t2a;
                        continue;
                    }
                    System.out.println("W-----------------------------");
                }
                this.setCaptain(1, party1.get(0).getId());
                this.setCurrentCaptain(party1.get(0).getId());
                if (party2.size() > 1) {
                    this.setCaptain(2, party2.get(0).getId());
                } else {
                    this.setCaptain(2, ((Member)modifiedplayers.get(0)).getId());
                }
                this.setTeam(1, team1);
                this.setTeam(2, team2);
            } else {
                this.setCaptains(players);
                this.setautomaticplayers(players, g2);
            }
            g2.moveVoiceMember(Objects.requireNonNull(g2.getMemberById(this.getCaptain(1))), gameVC1).queue();
            g2.moveVoiceMember(Objects.requireNonNull(g2.getMemberById(this.getCaptain(2))), gameVC2).queue();
            StringBuilder tm1 = new StringBuilder();
            StringBuilder tm2 = new StringBuilder();
            for (String s2 : new ArrayList<String>(this.getTeam(1))) {
                tm1.append("\u2022 <@!").append(s2).append(">\n");
                gameVC1.upsertPermissionOverride(Objects.requireNonNull(g2.getMemberById(s2))).setAllowed(Permission.VIEW_CHANNEL).setAllowed(Permission.VOICE_CONNECT).queue();
                if (!Objects.requireNonNull(Objects.requireNonNull(g2.getMemberById(s2)).getVoiceState()).inAudioChannel()) continue;
                g2.moveVoiceMember(Objects.requireNonNull(g2.getMemberById(s2)), gameVC1).queue();
            }
            for (String s2 : new ArrayList<String>(this.getTeam(2))) {
                tm2.append("\u2022 <@!").append(s2).append(">\n");
                gameVC2.upsertPermissionOverride(Objects.requireNonNull(g2.getMemberById(s2))).setAllowed(Permission.VIEW_CHANNEL).setAllowed(Permission.VOICE_CONNECT).queue();
                if (!Objects.requireNonNull(Objects.requireNonNull(g2.getMemberById(s2)).getVoiceState()).inAudioChannel()) continue;
                g2.moveVoiceMember(Objects.requireNonNull(g2.getMemberById(s2)), gameVC2).queue();
            }
            embed.addField("Team 1", tm1.toString(), true);
            embed.addField("Team 2", tm2.toString(), true);
            embed.addField("Randomly Picked Map", "**" + map.getDisplayName() + "** \u2014 `Height: " + map.getHeight() + "` (" + Utils.formatName(map.getTeam1().name()) + " vs " + Utils.formatName(map.getTeam2().name()) + ")", false);
            if (RBW.gamesAnnouncingChannel != null) {
                RBW.gamesAnnouncingChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            }
            Bukkit.getScheduler().runTaskLater((Plugin)com.deyo.rbw.ingame.Main.instance, () -> this.retry(map, g2), 20L);
        } else if (this.getMode().equals("captains")) {
            this.setCaptains(players);
            embed.addField("Team 1", "\u2022 <@!" + this.getCaptain(1) + "> *`(captain)`*", false);
            embed.addField("Team 2", "\u2022 <@!" + this.getCaptain(2) + "> *`(captain)`*", false);
            embed.setDescription("# <@!" + this.getCaptain(1) + "> please pick a player using `=pick <mention>`");
            StringBuilder remaining = new StringBuilder();
            for (Member m3 : players) {
                gameVC1.upsertPermissionOverride(m3).setAllowed(Permission.VIEW_CHANNEL).queue();
                g2.moveVoiceMember(m3, gameVC1).queue();
                if (this.getCaptain(1).equals(m3.getId()) || this.getCaptain(2).equals(m3.getId())) continue;
                remaining.append(m3.getAsMention()).append("\n");
            }
            embed.addField("Remaining", remaining.toString(), false);
        }
        ArrayList<String> playersString = new ArrayList<String>();
        for (Member m3 : players) {
            playersString.add(m3.getId());
        }
        playersString.remove(this.getCaptain(1));
        playersString.remove(this.getCaptain(2));
        this.setGameMembers(playersString);
        if (this.isCasual()) {
            embed.addField("This game is Casual", "It means that elo and other stats won't be affected", false);
        }
        ((MessageCreateAction)gameChannel.sendMessage(mentions.toString()).setEmbeds(embed.build())).queue();
    }

    public void retry(GameMap map, Guild g2) {
        this.setState(com.deyo.rbw.commands.types.GameState.PLAYING);
        this.setMap(map.getWorldName());
        ArrayList<String> players = new ArrayList<String>(this.getTeam(1));
        players.addAll(this.getTeam(2));
        IArena arena = map.getArena();
        Object mentions = "";
        for (String p : players) {
            mentions = (String)mentions + "<@" + p + ">";
        }
        TextChannel gameChannel = g2.getTextChannelById(this.getGameChannel());
        if (gameChannel == null) {
            return;
        }
        this.retries = (byte)(this.retries + 1);
        if (this.retries >= this.maxRetries) {
            BetterEmbed embed = new BetterEmbed("error", "Game Closed", "", "The game has been closed due to: `Max retries reached`...", "");
            ((MessageCreateAction)gameChannel.sendMessage((CharSequence)mentions).setEmbeds(embed.build())).queue();
            BetterEmbed closing = new BetterEmbed("default", "Closing in 10s", "", ":wastebasket: Report to developers if you believe this was an error.", "");
            gameChannel.sendMessageEmbeds(closing.build(), new MessageEmbed[0]).queue();
            gameChannel.upsertPermissionOverride(g2.getPublicRole()).deny(Permission.MESSAGE_SEND).queue();
            this.setState(com.deyo.rbw.commands.types.GameState.VOIDED);
            Main.runTaskLater(() -> this.deleteChats(g2, true), 10000L);
            return;
        }
        if (arena == null || arena.getStatus() == GameState.playing) {
            if (this.retries == 0) {
                ArrayList<GameMap> maps = GameMap.getFreeArenas(this.getMapType());
                Collections.shuffle(maps);
                if (maps.isEmpty()) {
                    return;
                }
                map = (GameMap)maps.get(0);
                BetterEmbed embed = new BetterEmbed("error", "Map rerolled", "", "The map has been rerolled to : `" + map.getDisplayName() + "`...", "");
                ((MessageCreateAction)gameChannel.sendMessage((CharSequence)mentions).setEmbeds(embed.build())).queue();
                GameMap finalMap = map;
                Bukkit.getScheduler().runTaskLater((Plugin)com.deyo.rbw.ingame.Main.instance, () -> this.retry(finalMap, g2), 100L);
            }
            return;
        }
        ArrayList<org.bukkit.entity.Player> minecraftPlayers = new ArrayList<org.bukkit.entity.Player>();
        for (String ID2 : players) {
            String player = Player.getName(ID2);
            org.bukkit.entity.Player p = Bukkit.getPlayer((String)player);
            if (p == null) {
                if (this.retries == this.maxRetries) {
                    BetterEmbed embed = new BetterEmbed("error", "Game Closed", "", "The game has been closed due to: `Max retries reached`...", "");
                    ((MessageCreateAction)gameChannel.sendMessage((CharSequence)mentions).setEmbeds(embed.build())).queue();
                    BetterEmbed closing = new BetterEmbed("default", "Closing in 10s", "", ":wastebasket: Report to developers if you believe this was an error.", "");
                    gameChannel.sendMessageEmbeds(closing.build(), new MessageEmbed[0]).queue();
                    gameChannel.upsertPermissionOverride(g2.getPublicRole()).deny(Permission.MESSAGE_SEND).queue();
                    this.setState(com.deyo.rbw.commands.types.GameState.VOIDED);
                    Main.runTaskLater(() -> this.deleteChats(g2, true), 10000L);
                } else {
                    BetterEmbed embed = new BetterEmbed("error", "Retrying", "", "`" + player + "` is not online.\n**Retrying in 15 seconds......**", "");
                    ((MessageCreateAction)gameChannel.sendMessage((CharSequence)mentions).setEmbeds(embed.build())).queue();
                    GameMap finalMap1 = map;
                    Bukkit.getScheduler().runTaskLater((Plugin)com.deyo.rbw.ingame.Main.instance, () -> this.retry(finalMap1, g2), 300L);
                }
                return;
            }
            minecraftPlayers.add(p);
        }
        ArenaUtils.warpAll(minecraftPlayers, map.getArena());
        RBWTeamAssigner.onGoingGames.put(map.getArena(), this);
    }

    private double getMatter(String ID2) {
        double value = 0.0;
        for (Statistic statistic : Statistic.values()) {
            if (!Config.getConfig().isSet("sort-by-" + statistic.getPath())) continue;
            int v = Config.getConfig().getInt("sort-by-" + statistic.getPath());
            if (v < 1) {
                v = 1;
            }
            value += statistic.getForPlayer(ID2) / 100.0 * (double)v;
        }
        return value;
    }

    public void setautomaticplayers(List<Member> players, Guild g2) {
        HashMap<String, Double> unsortedMap = new HashMap<String, Double>();
        ArrayList<Member> modifiedplayers = new ArrayList<Member>(players);
        modifiedplayers.remove(g2.getMemberById(this.getCaptain(1)));
        modifiedplayers.remove(g2.getMemberById(this.getCaptain(2)));
        ArrayList<String> team1 = new ArrayList<String>(this.getTeam(1));
        ArrayList<String> team2 = new ArrayList<String>(this.getTeam(2));
        for (Member m3 : modifiedplayers) {
            String ID2 = m3.getId();
            double value = this.getMatter(ID2);
            unsortedMap.put(ID2, value);
        }
        Map<String, Double> sortedMap = Utils.sortByValue(unsortedMap);
        LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(sortedMap.entrySet());
        for (int i = 0; i < modifiedplayers.size(); i += 2) {
            String[] values1 = ((Map.Entry<String, Double>)list.get(i)).toString().split("=");
            String[] values2 = ((Map.Entry<String, Double>)list.get(i + 1)).toString().split("=");
            team1.add(values1[0]);
            team2.add(values2[0]);
        }
        this.setTeam(1, team1);
        this.setTeam(2, team2);
    }

    public void setCaptains(List<Member> players) {
        ArrayList<String> playersString = new ArrayList<String>();
        for (Member m3 : players) {
            playersString.add(m3.getId());
        }
        Collections.shuffle(playersString);
        String captain1 = (String)playersString.get(0);
        String captain2 = (String)playersString.get(1);
        playersString.remove(captain1);
        playersString.remove(captain2);
        if (this.getMatter(captain1) > this.getMatter(captain2)) {
            this.setCaptain(1, captain2);
            this.setCaptain(2, captain1);
            this.setCurrentCaptain(captain2);
        } else {
            this.setCaptain(1, captain1);
            this.setCaptain(2, captain2);
            this.setCurrentCaptain(captain1);
        }
        ArrayList<String> team1 = new ArrayList<String>();
        ArrayList<String> team2 = new ArrayList<String>();
        team1.add(this.getCaptain(1));
        team2.add(this.getCaptain(2));
        this.setTeam(1, team1);
        this.setTeam(2, team2);
    }

    public void kickPlayersFromGame() {
        IArena arena = this.getArena();
        if (arena != null) {
            for (org.bukkit.entity.Player player : arena.getPlayers()) {
                arena.removePlayer(player, false);
                player.sendMessage("\u00a79===================================================");
                player.sendMessage("\u00a79");
                player.sendMessage("\u00a7b\u00a7lThe game you were playing has been voided. You have been safely removed.");
                player.sendMessage("\u00a79");
                player.sendMessage("\u00a79===================================================");
            }
            for (org.bukkit.entity.Player spectator : arena.getSpectators()) {
                arena.removeSpectator(spectator, false);
                spectator.sendMessage("\u00a79===================================================");
                spectator.sendMessage("\u00a79");
                spectator.sendMessage("\u00a7b\u00a7lThe game you were spectating has been voided. You have been safely removed.");
                spectator.sendMessage("\u00a79");
                spectator.sendMessage("\u00a79===================================================");
            }
        }
    }

    public BetterEmbed score(String scoredby, int winnerteam, ArrayList<String> mvps) {
        VoiceChannel waitingRoom;
        TextChannel gamechannel;
        String a;
        StatChangelog eloChangelog;
        StatChangelog mvpChangelog;
        StatChangelog eloChangelog2;
        Rank rank;
        int difference;
        int newElo;
        int oldElo;
        List<Integer> statslist;
        int modifiers;
        this.clearChangelogs();
        Guild g2 = RBW.mainGuild;
        if (Objects.equals(scoredby, "n")) {
            scoredby = g2.getSelfMember().getId();
        }
        this.setWinnerteam(winnerteam);
        BetterEmbed embed = new BetterEmbed("success", "Game`#" + this.gamenumber + "` Scored", "", "", "");
        this.setScoredBy(scoredby);
        if (Player.isPlayer(scoredby)) {
            StatChangelog changelog = new StatChangelog(scoredby, Statistic.SCORED, 1);
            changelog.run();
            this.storeChangelog(changelog);
        }
        this.setState(com.deyo.rbw.commands.types.GameState.SCORED);
        this.setMvps(mvps);
        ArrayList<String> team1 = new ArrayList<String>(this.getTeam(1));
        ArrayList<String> team2 = new ArrayList<String>(this.getTeam(2));
        ArrayList<String> players = new ArrayList<String>(team1);
        players.addAll(team2);
        for (String player : players) {
            StatChangelog kills = new StatChangelog(player, Statistic.KILLS, this.getKills(player));
            StatChangelog deaths = new StatChangelog(player, Statistic.DEATHS, this.getDeaths(player));
            StatChangelog beds = new StatChangelog(player, Statistic.BEDS, this.getBed(player));
            kills.run();
            deaths.run();
            beds.run();
            this.storeChangelog(kills);
            this.storeChangelog(deaths);
            this.storeChangelog(beds);
        }
        if (this.isCasual()) {
            embed.setDescription("This game is casual.\n Elo and stats won't be affected.");
            return embed;
        }
        StringBuilder t1 = new StringBuilder();
        StringBuilder t2 = new StringBuilder();
        Object mvs = "";
        Object beds = "";
        for (String s2 : team1) {
            modifiers = Modifiers.get(s2, this.gamenumber);
            statslist = winnerteam == 1 ? Player.win(s2, g2, modifiers, this) : Player.lose(s2, g2, modifiers, this);
            oldElo = statslist.get(0);
            newElo = statslist.get(1);
            difference = newElo - oldElo;
            rank = Rank.getPlayerRank(s2);
            if (rank == null) continue;
            if (this.getBed(s2) > 0) {
                int bedElo = rank.getBedElo();
                eloChangelog2 = new StatChangelog(s2, Statistic.ELO, bedElo);
                eloChangelog2.run();
                this.storeChangelog(eloChangelog2);
                newElo += bedElo;
                beds = (String)beds + "\u2022 <@" + s2 + ">\n";
                difference += bedElo;
            }
            if (mvps.contains(s2)) {
                int mvpElo = rank.getMvpElo();
                mvpChangelog = new StatChangelog(s2, Statistic.MVP, 1);
                mvpChangelog.run();
                this.storeChangelog(mvpChangelog);
                eloChangelog = new StatChangelog(s2, Statistic.ELO, mvpElo);
                eloChangelog.run();
                this.storeChangelog(eloChangelog);
                mvs = (String)mvs + "\u2022 <@" + s2 + ">\n";
                newElo += mvpElo;
                difference += mvpElo;
            }
            a = difference > 0 ? "+" : "";
            Player.fix(s2, g2);
            t1.append("\u2022 <@").append(s2).append("> **").append(a).append(difference).append("** `").append(oldElo).append("` > `").append(newElo).append("`\n");
        }
        for (String s2 : team2) {
            modifiers = Modifiers.get(s2, this.gamenumber);
            statslist = winnerteam == 1 ? Player.lose(s2, g2, modifiers, this) : Player.win(s2, g2, modifiers, this);
            oldElo = statslist.get(0);
            newElo = statslist.get(1);
            difference = newElo - oldElo;
            rank = Rank.getPlayerRank(s2);
            if (rank == null) continue;
            if (this.getBed(s2) > 0) {
                int bedElo = rank.getBedElo();
                eloChangelog2 = new StatChangelog(s2, Statistic.ELO, bedElo);
                eloChangelog2.run();
                this.storeChangelog(eloChangelog2);
                beds = (String)beds + "\u2022 <@" + s2 + ">\n";
                newElo += bedElo;
                difference += bedElo;
            }
            if (mvps.contains(s2)) {
                int mvpElo = rank.getMvpElo();
                mvpChangelog = new StatChangelog(s2, Statistic.MVP, 1);
                mvpChangelog.run();
                this.storeChangelog(mvpChangelog);
                eloChangelog = new StatChangelog(s2, Statistic.ELO, mvpElo);
                eloChangelog.run();
                this.storeChangelog(eloChangelog);
                mvs = (String)mvs + "\u2022 <@" + s2 + ">\n";
                newElo += mvpElo;
                difference += mvpElo;
            }
            a = difference > 0 ? "+" : "";
            Player.fix(s2, g2);
            t2.append("\u2022 <@").append(s2).append("> **").append(a).append(difference).append("** `").append(oldElo).append("` > `").append(newElo).append("`\n");
        }
        embed.addField("Team 1", t1.toString(), false);
        embed.addField("Team 2", t2.toString(), false);
        embed.addField("MVP", (String)mvs, false);
        if (!((String)beds).isEmpty()) {
            embed.addField("BED", (String)beds, false);
        }
        embed.addField("Scored by", "<@" + scoredby + ">", false);
        if (RBW.scoredAnnouncingChannel != null) {
            RBW.scoredAnnouncingChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
        }
        if ((gamechannel = g2.getTextChannelById(this.getGameChannel())) != null) {
            gamechannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            BetterEmbed closing = new BetterEmbed("default", "Closing in 1 minute", "", ":wastebasket: Deleting the channel in a minute \n**Reason:**  `Game Scored`", "");
            gamechannel.sendMessageEmbeds(closing.build(), new MessageEmbed[0]).queue();
            gamechannel.upsertPermissionOverride(g2.getPublicRole()).deny(Permission.MESSAGE_SEND).queue();
        }
        VoiceChannel vc1 = g2.getVoiceChannelById(this.getVoiceChannel(1));
        VoiceChannel vc2 = g2.getVoiceChannelById(this.getVoiceChannel(2));
        if (vc1 != null && vc2 != null && (waitingRoom = RBW.waitingRoom) != null) {
            for (Member member : vc1.getMembers()) {
                g2.moveVoiceMember(member, waitingRoom).queue();
            }
            for (Member member : vc2.getMembers()) {
                g2.moveVoiceMember(member, waitingRoom).queue();
            }
        }
        this.deleteChats(g2, false);
        return embed;
    }

    public MessageEmbed voidGame(String voidedby) {
        int playerNewElo;
        int playerOldElo;
        this.setState(com.deyo.rbw.commands.types.GameState.VOIDED);
        this.setScoredBy(voidedby);
        ArrayList<String> team1 = new ArrayList<String>(this.getTeam(1));
        ArrayList<String> team2 = new ArrayList<String>(this.getTeam(2));
        ArrayList<String> players = new ArrayList<String>(team1);
        players.addAll(team2);
        HashMap<String, Integer> oldElo = new HashMap<String, Integer>();
        for (String string : players) {
            oldElo.put(string, (int)Statistic.ELO.getForPlayer(string));
        }
        for (String string : players) {
            ArrayList<StatChangelog> changelogs = this.getChangelogs(string);
            for (StatChangelog statChangelog : changelogs) {
                statChangelog.revert();
            }
        }
        HashMap<String, Integer> newElo = new HashMap<String, Integer>();
        for (String player : players) {
            newElo.put(player, (int)Statistic.ELO.getForPlayer(player));
        }
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder t2 = new StringBuilder();
        Object beds = "";
        for (String ID2 : team1) {
            if (this.getBed(ID2) > 0) {
                beds = (String)beds + "\u2022 <@" + ID2 + ">\n";
            }
            playerOldElo = oldElo.getOrDefault(ID2, (int)Statistic.ELO.getForPlayer(ID2));
            playerNewElo = newElo.getOrDefault(ID2, (int)Statistic.ELO.getForPlayer(ID2));
            stringBuilder.append("\u2022 <@!").append(ID2).append("> `").append(playerOldElo).append("` *->* `").append(playerNewElo).append("` \n");
            Player.fix(ID2, RBW.mainGuild);
        }
        for (String ID2 : team2) {
            if (this.getBed(ID2) > 0) {
                beds = (String)beds + "\u2022 <@" + ID2 + ">\n";
            }
            playerOldElo = oldElo.getOrDefault(ID2, (int)Statistic.ELO.getForPlayer(ID2));
            playerNewElo = newElo.getOrDefault(ID2, (int)Statistic.ELO.getForPlayer(ID2));
            t2.append("\u2022 <@!").append(ID2).append("> `").append(playerOldElo).append("` *->* `").append(playerNewElo).append("` \n");
            Player.fix(ID2, RBW.mainGuild);
        }
        BetterEmbed betterEmbed = new BetterEmbed("default", "Game`#" + this.gamenumber + "` has been undone", "", "Elo and other stats fixed", "");
        betterEmbed.addField("Team 1", stringBuilder.toString(), false);
        betterEmbed.addField("Team 2", t2.toString(), false);
        Object mvs = "";
        for (String mvp : this.getMvps()) {
            mvs = (String)mvs + "\u2022 <@" + mvp + ">\n";
        }
        betterEmbed.addField("MVP", (String)mvs, false);
        if (!((String)beds).isEmpty()) {
            betterEmbed.addField("BED", (String)beds, false);
        }
        betterEmbed.addField("Undone by:", "<@" + voidedby + ">", false);
        return betterEmbed.build();
    }

    public static void loadGames() throws IOException {
        File a = new File("RBW/games");
        if (!a.exists()) {
            return;
        }
        File[] list = a.listFiles();
        if (list == null) {
            System.out.println("Game is null..");
            return;
        }
        if (list.length > 0) {
            for (File f : list) {
                int number = Integer.parseInt(f.getName().replaceAll(".yml", ""));
                new Game(number, f);
            }
            System.out.println("Successfully loaded all games into memory");
        }
    }

    public static void saveData() {
        try {
            for (Game game : games) {
                String jsonData = new Gson().toJson(game);
                File tempFile = new File(game.storing.getF().getAbsolutePath() + ".tmp");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    writer.write(jsonData);
                }
                if (!tempFile.renameTo(game.storing.getF())) {
                    throw new IOException("Failed to rename temp file to target file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isGameChannel(String id) {
        for (Game game : games) {
            if (game.getGameChannel() == null || !game.getGameChannel().equals(id)) continue;
            return true;
        }
        return false;
    }

    public static Game getFromNumber(int gamenumber) {
        Game result = null;
        for (Game game : games) {
            if (game.gamenumber != gamenumber) continue;
            result = game;
            break;
        }
        return result;
    }

    public static List<Game> getPlayerGames(String playerid, int limit) {
        return games.stream()
            .sorted((game, game1) -> Long.compare(game1.getStartedAt(), game.getStartedAt()))
            .filter(game -> game.getGameMembers().contains(playerid) || game.getTeam(1).contains(playerid) || game.getTeam(2).contains(playerid))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public static int getNumber(String ChannelID) {
        int result = -1;
        for (Game game : games) {
            if (game.getGameChannel() == null || !game.getGameChannel().equals(ChannelID)) continue;
            result = game.gamenumber;
            break;
        }
        return result;
    }

    private void delete(Guild g2) {
        Transcript t;
        TextChannel mainchat = g2.getTextChannelById(this.getGameChannel());
        VoiceChannel vc1 = g2.getVoiceChannelById(this.getVoiceChannel(1));
        VoiceChannel vc2 = g2.getVoiceChannelById(this.getVoiceChannel(2));
        if (mainchat != null) {
            mainchat.delete().queue();
        }
        if (vc1 != null) {
            vc1.delete().queue();
        }
        if (vc2 != null) {
            vc2.delete().queue();
        }
        if ((t = Transcript.getTranscript(this.getGameChannel())) != null) {
            File file = t.toFile();
            TextChannel logs = RBW.gameLogsChannel;
            if (logs != null) {
                logs.sendFiles(FileUpload.fromData(file)).queue();
            }
        }
    }

    public void deleteChats(final Guild g2, boolean now) {
        long time = now ? 1000L : 60000L;
        Timer timer = new Timer();
        TimerTask taska = new TimerTask(){

            @Override
            public void run() {
                Game.this.delete(g2);
            }
        };
        timer.schedule(taska, time);
    }
}

