/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.ingame.commands;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.classes.guild.Guild;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;

public class GuildCommand
implements CommandExecutor {
    /*
     * Enabled aggressive block sorting
     */
    public boolean onCommand(CommandSender commandSender, Command command, String s2, String[] args2) {
        String targetId;
        if (!(commandSender instanceof org.bukkit.entity.Player)) {
            commandSender.sendMessage("\u00a74Only fkn players can execute this.");
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player)commandSender;
        String ID2 = Player.getIdFromIGN(player.getName());
        if (ID2 == null) {
            player.sendMessage("\u00a7cYou need to be registered on discord to execute this command.");
            return true;
        }
        if (args2.length < 1) {
            GuildCommand.sendHelp(player, s2);
            return true;
        }
        if (args2[0].equalsIgnoreCase("create")) {
            if (args2.length != 2) {
                player.sendMessage("\u00a7c\u00a7lSpecify the name of the guild to create!");
                return true;
            }
            int minGold = Config.getConfig().getInt("guilds.min-gold", 0);
            if (Statistic.GOLD.getForPlayer(ID2) < (double)minGold) {
                player.sendMessage("\u00a7c\u00a7lYou don't have enough gold to create a guild, You need a minimum of " + minGold + " gold");
                return true;
            }
            if (Guild.getGuildByMember(ID2) != null) {
                player.sendMessage("\u00a7c\u00a7lYou are already in a guild, Please leave your current guild.");
                return true;
            }
            String guildName = args2[1];
            if (Guild.getGuildByName(guildName) != null) {
                player.sendMessage("\u00a7c\u00a7lA guild with that name already exists.");
                return true;
            }
            if (guildName.length() > 16) {
                player.sendMessage("\u00a7c\u00a7lA guild name can be 16 characters max.");
                return true;
            }
            new Guild(guildName, ID2);
            Statistic.GOLD.setForPlayer(ID2, Statistic.GOLD.getForPlayer(ID2) - (double)minGold);
            player.sendMessage("\u00a7aYou successfully created a guild named " + guildName);
            return true;
        }
        if (args2[0].equalsIgnoreCase("demote")) {
            if (args2.length != 2) {
                player.sendMessage("\u00a7c\u00a7lPlease specify a valid player to demote from the guild!");
                return true;
            }
            Guild guild = Guild.getGuildByMember(ID2);
            if (guild == null) {
                player.sendMessage("\u00a7c\u00a7lYou are currently not in a guild.");
                return true;
            }
            if (!guild.getLeader().equalsIgnoreCase(ID2)) {
                player.sendMessage("\u00a7c\u00a7lOnly the leader can demote members of the guild!");
                return true;
            }
            String memberID = Player.getIdFromIGN(args2[1]);
            if (memberID == null) {
                player.sendMessage("\u00a7c\u00a7lPlease specify a valid member to demote.");
                return true;
            }
            if (!guild.getOfficers().contains(memberID)) {
                player.sendMessage("\u00a7c\u00a7lThis player is not officer.");
                return true;
            }
            guild.removeOfficer(memberID);
            player.sendMessage("\u00a7a\u00a7lSuccessfully demoted " + args2[1] + " from Guild Officer");
            return true;
        }
        if (args2[0].equalsIgnoreCase("disband")) {
            Guild guild = Guild.getGuildByMember(ID2);
            if (guild == null) {
                player.sendMessage("\u00a7c\u00a7lYou are currently not in a guild.");
                return true;
            }
            if (!guild.getLeader().equalsIgnoreCase(ID2)) {
                player.sendMessage("\u00a7c\u00a7lYou are not the guild leader. Please use =guild leave to leave the guild!");
                return true;
            }
            guild.setMembers(new ArrayList<String>());
            guild.setOfficers(new ArrayList<String>());
            guild.disband();
            player.sendMessage("\u00a7a\u00a7lYou disbanded the guild **" + guild.getGuildName() + "**!");
            return true;
        }
        if (!args2[0].equalsIgnoreCase("info") && !args2[0].equalsIgnoreCase("list")) {
            GuildCommand.sendHelp(player, s2);
            return true;
        }
        if (args2.length == 1) {
            targetId = ID2;
        } else {
            org.bukkit.entity.Player target = Bukkit.getPlayer((String)args2[1]);
            if (target == null) {
                Guild guildByName = Guild.getGuildByName(args2[1]);
                if (guildByName == null) {
                    player.sendMessage("\u00a7c\u00a7lCouldn't find a guild with that name.");
                    return true;
                }
                targetId = guildByName.getLeader();
            } else {
                targetId = Player.getIdFromIGN(target.getName());
            }
        }
        Guild guild = Guild.getGuildByMember(targetId);
        if (guild == null) {
            if (targetId.equalsIgnoreCase(ID2)) {
                player.sendMessage("\u00a7c\u00a7lYou are currently not in a guild.");
                return true;
            }
            player.sendMessage("\u00a7c\u00a7lThe mentioned player is not currently in a guild.");
            return true;
        }
        ArrayList<String> members = guild.getMembers();
        int totalMembers = members.size();
        ArrayList<String> officers = guild.getOfficers();
        String guildLeader = guild.getLeader();
        members.removeAll(officers);
        members.remove(guildLeader);
        player.sendMessage("\u00a76=======================================");
        player.sendMessage("\u00a76   \ud83c\udff0 Guild Information - " + guild.getGuildName());
        player.sendMessage("\u00a76=======================================");
        player.sendMessage("\u00a7eGuild Structure:");
        player.sendMessage("\u00a7bLeader: \u00a7f<" + guildLeader + ">");
        if (!officers.isEmpty()) {
            player.sendMessage("\u00a7bOfficers (" + officers.size() + "):");
            for (String officerId : officers) {
                player.sendMessage("\u00a7f \u2022 <" + officerId + ">");
            }
        }
        if (!members.isEmpty()) {
            player.sendMessage("\u00a7bMembers (" + members.size() + "):");
            for (String memberId : members) {
                player.sendMessage("\u00a7f \u2022 <" + memberId + ">");
            }
        }
        player.sendMessage("\u00a77Total Members: " + totalMembers);
        player.sendMessage("\u00a76=======================================");
        return true;
    }

    public static void sendHelp(org.bukkit.entity.Player player, String s2) {
        player.sendMessage("\u00a76\u00a7l===============================================");
        player.sendMessage("\u00a76               Guild Commands");
        player.sendMessage("\u00a76\u00a7l===============================================");
        player.sendMessage("\u00a7e" + s2 + " create <name> \u00a77- Create a new guild.");
        player.sendMessage("\u00a7e" + s2 + " invite <player> \u00a77- Invite a player to your guild.");
        player.sendMessage("\u00a7e" + s2 + " join \u00a77- Join the guild you were invited to.");
        player.sendMessage("\u00a7e" + s2 + " promote <player> \u00a77- Promote a player to guild officer.");
        player.sendMessage("\u00a7e" + s2 + " demote <player> \u00a77- Demote a guild officer to member.");
        player.sendMessage("\u00a7e" + s2 + " transfer <player> \u00a77- Transfer guild leadership to a player.");
        player.sendMessage("\u00a7e" + s2 + " leave \u00a77- Leave your current guild.");
        player.sendMessage("\u00a7e" + s2 + " list \u00a77- List all members of your guild.");
        player.sendMessage("\u00a7e" + s2 + " disband \u00a77- Disband your guild.");
        player.sendMessage("\u00a7e" + s2 + " kick <player> \u00a77- Kick a player from your guild.");
        player.sendMessage("\u00a7e" + s2 + " info \u00a77- Show information about your guild.");
        player.sendMessage("\u00a76\u00a7l===============================================");
    }
}

