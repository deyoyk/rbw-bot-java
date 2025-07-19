/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player.party;

import java.util.ArrayList;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.Parties;
import com.deyo.rbw.classes.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class PartyWarp {
    public static void execute(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        if (Parties.HasParty(m3.getId())) {
            if (!m3.getVoiceState().inAudioChannel()) {
                BetterEmbed embed = new BetterEmbed("error", "Error", "", "You are not in a voice channel.", "");
                embed.reply(msg);
                return;
            }
            ArrayList<String> members = Parties.getPartyMembers(m3.getId());
            for (String member : members) {
                g2.moveVoiceMember(g2.getMemberById(member), m3.getVoiceState().getChannel()).complete();
            }
            BetterEmbed embed = new BetterEmbed("success", "Warped", Utils.avatar(m3.getUser().getAvatarUrl()), "Successfully warped your party into your vc.", "");
            embed.reply(msg);
        } else {
            BetterEmbed embed = new BetterEmbed("error", "Error", "", "Only party owner can run this command.", "");
            embed.reply(msg);
        }
    }
}

