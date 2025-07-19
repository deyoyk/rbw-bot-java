/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.RBW;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class Parties {
    public static HashMap<String, ArrayList<String>> parties = new HashMap();
    public static ArrayList<String> autoWarps = new ArrayList();
    public static HashMap<String, Long> expirations = new HashMap();
    public static HashMap<String, String> partyinvites = new HashMap();

    public static void load() {
        TimerTask deleteparty = new TimerTask(){

            @Override
            public void run() {
                ArrayList<String> keysToRemove = new ArrayList<String>();
                for (Map.Entry<String, Long> entry : expirations.entrySet()) {
                    String key = entry.getKey();
                    Long value = entry.getValue();
                    long a = System.currentTimeMillis();
                    long seconds = (a - value) / 1000L;
                    if (seconds <= 1300L) continue;
                    BetterEmbed embed = new BetterEmbed("success", "Party disbanded", "", "Your party was automatically disbanded!", "");
                    RBW.alertsChannel.sendMessage("<@" + key + ">").queue();
                    RBW.alertsChannel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
                    keysToRemove.add(key);
                    Parties.deleteParty(key);
                }
                for (String keyToRemove : keysToRemove) {
                    expirations.remove(keyToRemove);
                }
            }
        };
        Timer as = new Timer();
        as.schedule(deleteparty, 0L, 300000L);
        System.out.println("Loaded Parties Successfully!");
    }

    public static void addInvite(final String invited, String partyowner) {
        partyinvites.put(invited, partyowner);
        TimerTask taskcasual = new TimerTask(){

            @Override
            public void run() {
                Parties.removeInvite(invited);
            }
        };
        Timer timercasual = new Timer();
        timercasual.schedule(taskcasual, 900000L);
    }

    public static void removeInvite(String invited) {
        partyinvites.remove(invited);
    }

    public static boolean HasParty(String ID2) {
        return parties.containsKey(ID2);
    }

    public static boolean isInParty(String ID2) {
        boolean b = parties.containsKey(ID2);
        for (ArrayList<String> list : parties.values()) {
            if (!list.contains(ID2)) continue;
            return true;
        }
        return b;
    }

    public static int PartyLength(String partyOwner) {
        if (!Parties.HasParty(partyOwner)) {
            return 0;
        }
        ArrayList<String> members = parties.get(partyOwner);
        return members.size();
    }

    public static void createParty(String PartyOwner, ArrayList<String> members) {
        if (Parties.HasParty(PartyOwner)) {
            Parties.deleteParty(PartyOwner);
            parties.put(PartyOwner, members);
            expirations.put(PartyOwner, System.currentTimeMillis());
        } else {
            parties.put(PartyOwner, members);
        }
    }

    public static void deleteParty(String PartyOwner) {
        if (!Parties.HasParty(PartyOwner)) {
            return;
        }
        parties.remove(PartyOwner);
        autoWarps.remove(PartyOwner);
        expirations.remove(PartyOwner);
    }

    public static boolean isUserInParty(String PartyOwner, String User2) {
        if (!Parties.HasParty(PartyOwner)) {
            return false;
        }
        ArrayList<String> members = parties.get(PartyOwner);
        return members.contains(User2);
    }

    public static boolean kick(String PartyOwner, String User2) {
        if (!Parties.isUserInParty(PartyOwner, User2)) {
            return false;
        }
        if (PartyOwner.equalsIgnoreCase(User2)) {
            return false;
        }
        ArrayList<String> members = parties.get(PartyOwner);
        members.remove(User2);
        if (members.size() == 1) {
            parties.remove(PartyOwner);
        }
        if (members.size() > 1) {
            parties.put(PartyOwner, members);
        }
        return true;
    }

    public static ArrayList<String> getPartyMembers(String PartyOwner) {
        if (!Parties.HasParty(PartyOwner)) {
            return new ArrayList<String>();
        }
        expirations.remove(PartyOwner);
        expirations.put(PartyOwner, System.currentTimeMillis());
        return parties.get(PartyOwner);
    }

    public static void promote(String newOwner, String oldOwner) {
        if (!Parties.isUserInParty(oldOwner, newOwner)) {
            return;
        }
        if (newOwner.equalsIgnoreCase(oldOwner)) {
            return;
        }
        ArrayList<String> members = parties.get(oldOwner);
        Parties.deleteParty(oldOwner);
        Parties.createParty(newOwner, members);
    }
}

