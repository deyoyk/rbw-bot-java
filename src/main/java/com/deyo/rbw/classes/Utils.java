/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;

public class Utils {
    public static String formatName(String a) {
        String e = a.toLowerCase();
        String firstChar = e.substring(0, 1);
        firstChar = firstChar.toUpperCase();
        return firstChar + e.substring(1);
    }

    public static int CountElo(ArrayList<String> ids) {
        int count = 0;
        for (String id : ids) {
            count = (int)((double)count + Statistic.ELO.getForPlayer(id));
        }
        return count;
    }

    public static String avatar(String avatar) {
        if (avatar == null) {
            avatar = "";
        }
        return avatar;
    }

    public static Member getArg(String arg, Guild g2) {
        Member member = null;
        String ID2 = arg.replaceAll("[^0-9]", "");
        String f = Player.getIdFromIGN(arg);
        if (f != null) {
            ID2 = f;
        }
        try {
            member = (Member)g2.retrieveMemberById(ID2).complete();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return member;
    }

    public static boolean isInteger(String s2) {
        try {
            Integer.parseInt(s2);
        }
        catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String s2) {
        try {
            Long.parseLong(s2);
        }
        catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static int getRandomNumber(int min2, int max) {
        return (int)(Math.random() * (double)(max - min2) + (double)min2);
    }

    public static String getUUID(String name) {
        try {
            int read;
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            StringBuilder result = new StringBuilder();
            while ((read = reader.read()) != -1) {
                result.append((char)read);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject)jsonParser.parse(result.toString());
            return (String)json.get("id");
        }
        catch (IOException | ParseException exception) {
            return "1be04a48-aaf6-4eea-af63-845f321bf7af";
        }
    }

    public static int randomInt(Integer min2, Integer max) {
        Random random = new Random();
        return random.nextInt(max - min2 + 1) + min2;
    }

    public static Map<String, Double> sortByValue(Map<String, Double> unsortedMap) {
        LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortedMap.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry entry : list) {
            sortedMap.put((String)entry.getKey(), (Double)entry.getValue());
        }
        return sortedMap;
    }
}

