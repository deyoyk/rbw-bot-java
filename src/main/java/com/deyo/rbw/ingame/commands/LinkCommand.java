/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.deyo.rbw.ingame.commands;

import java.io.File;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.ingame.PINHandling;

public class LinkCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s2, String[] args2) {
        if (commandSender instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player p = (org.bukkit.entity.Player)commandSender;
            if (args2.length >= 1) {
                int pin;
                try {
                    pin = Integer.parseInt(args2[0]);
                }
                catch (Exception ex) {
                    p.sendMessage("\u00a74Please specify a valid PIN.");
                    return true;
                }
                String[] IDs = PINHandling.getIdByPin(pin);
                if (IDs == null) {
                    p.sendMessage("\u00a74Please specify a valid PIN.");
                    return true;
                }
                String ingamepname = p.getName();
                if (!IDs[1].equalsIgnoreCase(ingamepname)) {
                    p.sendMessage("\u00a74This PIN is linked to another IGN.");
                    return true;
                }
                String ID2 = IDs[0];
                String oldID = Player.getIdFromIGN(p.getName());
                if (oldID != null && !oldID.equals(ID2)) {
                    p.sendMessage("\u00a7cYour account is already linked to \u00a7e" + oldID + "\u00a7c please contact staff to unregister your old account.");
                    return true;
                }
                Member member = (Member)RBW.mainGuild.retrieveMemberById(ID2).complete();
                if (member == null) {
                    System.out.println("\u00a7cUnknown error.");
                    return true;
                }
                File f = new File("RBW/players/" + ID2 + ".yml");
                if (f.exists()) {
                    if (Player.getName(ID2).equals(ingamepname)) {
                        System.out.println("\u00a7cYou are already linked to that...");
                        return true;
                    }
                    Player.setName(ID2, ingamepname);
                } else {
                    Player.createFile(ID2, ingamepname);
                }
                Player.fix(ID2, RBW.mainGuild);
                p.sendMessage("\u00a7aSuccessfully linked to " + member.getEffectiveName());
            }
        } else {
            System.out.println("I don't think console could execute this..");
        }
        return true;
    }
}

