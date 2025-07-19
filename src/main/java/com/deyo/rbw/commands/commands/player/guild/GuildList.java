/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.guild;

import java.lang.invoke.CallSite;
import java.util.ArrayList;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class GuildList {
    /*
     * Enabled aggressive block sorting
     */
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        String ID2;
        if (args2.length == 2) {
            ID2 = m3.getId();
        } else {
            Member member = Utils.getArg(args2[2], g2);
            if (member == null) {
                com.deyo.rbw.classes.guild.Guild g1 = com.deyo.rbw.classes.guild.Guild.getGuildByName(args2[2]);
                if (g1 == null) {
                    BetterEmbed error = new BetterEmbed("error", "Error", "", "Couldn't find a guild with that name.", "");
                    error.reply(msg);
                    return;
                }
                ID2 = g1.getLeader();
            } else {
                ID2 = member.getId();
            }
        }
        com.deyo.rbw.classes.guild.Guild guild = com.deyo.rbw.classes.guild.Guild.getGuildByMember(ID2);
        if (guild == null) {
            BetterEmbed error;
            if (ID2.equalsIgnoreCase(m3.getId())) {
                error = new BetterEmbed("error", "Error", "", "You are currently not in a guild.", "");
                error.reply(msg);
                return;
            }
            error = new BetterEmbed("error", "Error", "", "The mentioned player is not currently in a guild.", "");
            error.reply(msg);
            return;
        }
        ArrayList<String> members = guild.getMembers();
        int membersSize = members.size();
        ArrayList<String> officers = guild.getOfficers();
        String guildLeader = guild.getLeader();
        members.removeAll(officers);
        members.remove(guildLeader);
        String leaderName = "<@" + guildLeader + ">";
        ArrayList<String> officerNames = new ArrayList<>();
        for (String string : officers) {
            officerNames.add("\u2022 <@" + string + ">");
        }
        ArrayList<String> memberNames = new ArrayList<>();
        for (String memberId : members) {
            memberNames.add("\u2022 <@" + memberId + ">");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\ud83c\udff0 **Guild Structure** \ud83c\udff0\n\n");
        stringBuilder.append("\ud83d\udc51 **Leader**\n").append(leaderName).append("\n");
        if (!officerNames.isEmpty()) {
            stringBuilder.append("\n\ud83c\udf96\ufe0f **Officers** (").append(officerNames.size()).append(")\n");
            stringBuilder.append(String.join("\n", officerNames));
        }
        if (!memberNames.isEmpty()) {
            stringBuilder.append("\n\n\ud83d\udc65 **Members** (").append(memberNames.size()).append(")\n");
            stringBuilder.append(String.join("\n", memberNames));
        }
        stringBuilder.append("\n\n\ud83d\udcca **Total Members:** ").append(membersSize);
        String description = stringBuilder.toString();
        BetterEmbed embed = new BetterEmbed("success", "\ud83c\udff0 Guild Information - " + guild.getGuildName(), "", description, "");
        embed.reply(msg);
    }
}

