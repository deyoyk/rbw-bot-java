/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes.guild;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.deyo.rbw.childclasses.configuration.file.YamlConfiguration;
import com.deyo.rbw.classes.guild.GuildStoring;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Member;

public class Guild {
    public static ArrayList<Guild> guilds = new ArrayList();
    private final String guildName;
    private final GuildStoring storing;
    private final YamlConfiguration config;
    private final ArrayList<Member> invitedMembers = new ArrayList();

    public Guild(String guildName) {
        this.storing = new GuildStoring(guildName);
        this.guildName = guildName;
        this.config = this.storing.getConfig();
        guilds.add(this);
    }

    public Guild(String guildName, String leaderID) {
        this.storing = new GuildStoring(guildName);
        this.guildName = guildName;
        this.config = this.storing.getConfig();
        this.setLeader(leaderID);
        this.setMembers(new ArrayList<String>(List.of(leaderID)));
        guilds.add(this);
    }

    public void disband() {
        guilds.remove(this);
        this.storing.remove();
    }

    public static void loadGuilds() {
        File a = new File("RBW/guilds");
        if (!a.exists()) {
            return;
        }
        File[] list = a.listFiles();
        if (list == null) {
            System.out.println("Guild is null..");
            return;
        }
        if (list.length > 0) {
            for (File f : list) {
                String guildName = f.getName().replaceAll(".yml", "");
                new Guild(guildName);
            }
            System.out.println("Successfully loaded all guilds into memory");
        }
    }

    public static void saveData() {
        try {
            for (Guild guild : guilds) {
                guild.config.save(guild.storing.getF());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInvitedMember(final Member m3) {
        this.invitedMembers.add(m3);
        TimerTask taskcasual = new TimerTask(){

            @Override
            public void run() {
                Guild.this.removeInvitedMember(m3);
            }
        };
        Timer timercasual = new Timer();
        timercasual.schedule(taskcasual, 900000L);
    }

    public void removeInvitedMember(Member m3) {
        this.invitedMembers.remove(m3);
    }

    public ArrayList<Member> getInvitedMembers() {
        return this.invitedMembers;
    }

    public String getGuildName() {
        return this.guildName;
    }

    public void setLeader(String leaderID) {
        this.config.set("leader", leaderID);
    }

    public String getLeader() {
        return this.config.getString("leader", "");
    }

    public void setMembers(ArrayList<String> members) {
        this.config.set("members", members);
    }

    public ArrayList<String> getMembers() {
        return new ArrayList<String>(this.config.getStringList("members"));
    }

    public void addMember(String member) {
        ArrayList<String> currentMembers = this.getMembers();
        currentMembers.add(member);
        this.setMembers(currentMembers);
    }

    public void removeMember(String memberID) {
        ArrayList<String> currentMembers = this.getMembers();
        currentMembers.remove(memberID);
        this.setMembers(currentMembers);
    }

    public void setOfficers(ArrayList<String> members) {
        this.config.set("officers", members);
    }

    public ArrayList<String> getOfficers() {
        return new ArrayList<String>(this.config.getStringList("officers"));
    }

    public void addOfficer(String member) {
        ArrayList<String> currentMembers = this.getOfficers();
        currentMembers.add(member);
        this.setOfficers(currentMembers);
    }

    public void removeOfficer(String memberID) {
        ArrayList<String> currentMembers = this.getOfficers();
        currentMembers.remove(memberID);
        this.setOfficers(currentMembers);
    }

    public int getStatistic(Statistic statistic) {
        int elo = 0;
        for (String member : this.getMembers()) {
            elo += (int)statistic.getForPlayer(member);
        }
        return elo;
    }

    public static Guild getGuildByMember(String ID2) {
        for (Guild guild : guilds) {
            if (!guild.getMembers().contains(ID2)) continue;
            return guild;
        }
        return null;
    }

    public static Guild getGuildByName(String name) {
        for (Guild guild : guilds) {
            if (!guild.getGuildName().equalsIgnoreCase(name)) continue;
            return guild;
        }
        return null;
    }

    public static Guild getGuildByLeader(String ID2) {
        for (Guild guild : guilds) {
            if (!guild.getLeader().equalsIgnoreCase(ID2)) continue;
            return guild;
        }
        return null;
    }
}

