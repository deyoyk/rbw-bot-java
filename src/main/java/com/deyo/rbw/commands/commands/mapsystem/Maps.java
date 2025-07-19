/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.mapsystem;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.classes.GameMap;
import com.deyo.rbw.classes.Queue;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Maps
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        try {
            Object maps = "";
            if (args2.length > 1) {
                Object mapSearching = args2[1].equalsIgnoreCase("4v4") ? Queue.QueueMaps.Quads : Queue.QueueMaps.Doubles;
                for (GameMap instance : GameMap.getMaps()) {
                    if (instance.getMapType() != mapSearching) continue;
                    maps = (String)maps + "**" + instance.getDisplayName() + "** \u2014 `Height: " + instance.getHeight() + "` \n";
                }
            } else {
                for (GameMap instance : GameMap.getMaps()) {
                    maps = (String)maps + "**" + instance.getDisplayName() + "** \u2014 `Height: " + instance.getHeight() + "` \n";
                }
            }
            BetterEmbed embed = ((String)maps).isEmpty() ? new BetterEmbed("error", "Error", "", "There are no maps set!", "") : new BetterEmbed("default", "All maps", "", (String)maps, "");
            embed.reply(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

