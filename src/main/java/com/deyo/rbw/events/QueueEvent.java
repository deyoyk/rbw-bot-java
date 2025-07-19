/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;

public class QueueEvent
extends ListenerAdapter {
    public static YamlConfiguration serverData = YamlConfiguration.loadConfiguration(new File("RBW/serverstats.yml"));
    public static boolean cooldown = false;

    public static void checkAutoWarp(Member m3, Guild g2) {
        if (Parties.autoWarps.contains(m3.getId())) {
            for (String partyMember : Parties.getPartyMembers(m3.getId())) {
                Member member = g2.getMemberById(partyMember);
                if (member == null || member.getId().equals(m3.getId())) continue;
                try {
                    if (member.getVoiceState() == null || m3.getVoiceState() == null) continue;
                    g2.moveVoiceMember(member, m3.getVoiceState().getChannel()).queue();
                }
                catch (Exception exception) {}
            }
        }
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        TextChannel alerts = RBW.alertsChannel;
        if (event.getChannelJoined() == null) {
            return;
        }
        Queue q = Queue.getQueueFromID(event.getChannelJoined().getId());
        if (q != null) {
            VoiceChannel waitingRoom = RBW.waitingRoom;
            if (new File("RBW/bans/" + event.getMember().getId() + ".yml").exists()) {
                if (waitingRoom != null) {
                    event.getGuild().moveVoiceMember(event.getMember(), waitingRoom).queue();
                } else {
                    event.getGuild().kickVoiceMember(event.getMember()).queue();
                }
                BetterEmbed embed = new BetterEmbed("error", "You Can't Queue", "", "", "");
                embed.addField("It appears that you've been banned", "If this is a mistake, please do `=fix`. If it still doesn't remove your banned role, open an appeal ticket", false);
                if (alerts != null) {
                    ((MessageCreateAction)alerts.sendMessage(event.getMember().getAsMention()).setEmbeds(embed.build())).queue();
                }
            } else {
                if (!new File("RBW/players/" + event.getMember().getId() + ".yml").exists()) {
                    if (waitingRoom != null) {
                        event.getGuild().moveVoiceMember(event.getMember(), waitingRoom).queue();
                    } else {
                        event.getGuild().kickVoiceMember(event.getMember()).queue();
                    }
                    return;
                }
                org.bukkit.entity.Player player = Bukkit.getPlayer((String)Player.getName(event.getMember().getId()));
                if (player == null) {
                    if (waitingRoom != null) {
                        event.getGuild().moveVoiceMember(event.getMember(), waitingRoom).queue();
                    } else {
                        event.getGuild().kickVoiceMember(event.getMember()).queue();
                    }
                    BetterEmbed embed = new BetterEmbed("error", "You Can't Queue", "", "", "");
                    embed.addField("Not online", "You are not online in the minecraft server. Please join the server `" + com.deyo.rbw.ingame.Main.serverIp + "` then try queueing again", false);
                    if (alerts != null) {
                        ((MessageCreateAction)alerts.sendMessage(event.getMember().getAsMention()).setEmbeds(embed.build())).queue();
                    }
                    return;
                }
                QueueEvent.checkAutoWarp(event.getMember(), event.getGuild());
                int playersInTeam = q.getPlayersInTeam();
                ArrayList<Member> verifiedpeople = new ArrayList<Member>();
                ArrayList<Member> party1 = new ArrayList<Member>();
                ArrayList<Member> party2 = new ArrayList<Member>();
                for (Member member : event.getChannelJoined().getMembers()) {
                    String id2 = member.getId();
                    if (Parties.HasParty(id2)) {
                        Member a;
                        int partyNum;
                        ArrayList<String> members = Parties.getPartyMembers(id2);
                        int diff = playersInTeam * 2 - verifiedpeople.size();
                        if (members.size() > playersInTeam || members.size() > diff) continue;
                        if (party1.size() + members.size() <= playersInTeam) {
                            partyNum = 1;
                        } else {
                            if (party2.size() + members.size() > playersInTeam) continue;
                            partyNum = 2;
                        }
                        boolean IsInVc = true;
                        for (String n : members) {
                            a = (Member)event.getGuild().retrieveMemberById(n).complete();
                            if (event.getChannelJoined().getMembers().contains(a)) continue;
                            IsInVc = false;
                        }
                        if (!IsInVc) continue;
                        for (String n : members) {
                            a = (Member)event.getGuild().retrieveMemberById(n).complete();
                            verifiedpeople.add(a);
                            if (partyNum == 1) {
                                party1.add(a);
                                continue;
                            }
                            party2.add(a);
                        }
                        continue;
                    }
                    if (Parties.isInParty(id2)) continue;
                    verifiedpeople.add(member);
                }
                if (verifiedpeople.size() > playersInTeam * 2) {
                    List toremove = verifiedpeople.stream().map(ISnowflake::getId).filter(id -> !Parties.isInParty(id)).collect(Collectors.toList());
                    int diff = verifiedpeople.size() - playersInTeam * 2;
                    for (int i = 0; i < diff && i != toremove.size(); ++i) {
                        String f = (String)toremove.get(i);
                        verifiedpeople.removeIf(m3 -> f.equals(m3.getId()));
                    }
                }
                if (verifiedpeople.size() == playersInTeam * 2) {
                    if (cooldown) {
                        return;
                    }
                    cooldown = true;
                    Main.runTaskLater(() -> {
                        cooldown = false;
                    }, 4000L);
                    System.out.println("Queued a Game.");
                    try {
                        int gameNumber = ServerConfig.get().getInt("games-played", 0) + 1;
                        ServerConfig.get().set("games-played", gameNumber);
                        Game game = new Game(gameNumber, q);
                        game.startGame(verifiedpeople, event.getGuild(), party1, party2);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class ServerConfig {
        public static YamlConfiguration get() {
            return serverData;
        }

        public static void save() {
            try {
                serverData.save(new File("RBW/serverstats.yml"));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

