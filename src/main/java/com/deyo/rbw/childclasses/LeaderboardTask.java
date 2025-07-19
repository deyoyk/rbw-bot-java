/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CachedPlayer;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;
import com.deyo.rbw.events.QueueEvent;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

public class LeaderboardTask {
    public static void initTask() {
        TimerTask everyday = new TimerTask(){

            @Override
            public void run() {
                try {
                    LeaderboardTask.check();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Leaderboard-Refresh");
        timer.schedule(everyday, 0L, TimeUnit.HOURS.toMillis(12L));
    }

    private static void check() throws IOException {
        long last = QueueEvent.ServerConfig.get().getLong("last-lb", 0L);
        long now = System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(now - last);
        if (last == 0L || days >= 1L) {
            QueueEvent.ServerConfig.get().set("last-lb", now);
            DecimalFormat formatter = new DecimalFormat("#0");
            TextChannel channel = RBW.leaderboardUpdatesChannel;
            if (channel == null) {
                return;
            }
            HashMap<String, Double> unsortedMap = new HashMap<String, Double>();
            for (String ID2 : Player.getPlayers().keySet()) {
                unsortedMap.put(ID2, Statistic.ELO.getForPlayer(ID2));
            }
            Map<String, Double> sortedMap = Utils.sortByValue(unsortedMap);
            LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(sortedMap.entrySet());
            File lbImage = new File("RBW/leaderboard.png");
            if (lbImage.exists()) {
                BufferedImage lb = ImageIO.read(lbImage);
                Graphics2D gfx = (Graphics2D)lb.getGraphics();
                gfx.setBackground(Color.BLACK);
                gfx.setColor(Color.BLACK);
                gfx.setFont(new Font("Minecraft", 0, Config.getConfig().getInt("lb-fontsize")));
                for (int i = 0; i < 10; ++i) {
                    String PlayerUUID;
                    if (i >= list.size()) continue;
                    String[] values2 = ((Map.Entry)list.get(i)).toString().split("=");
                    String ID3 = values2[0];
                    int yvar = Config.getConfig().getInt("lb-y-variation");
                    gfx.setColor(new Color(204, 122, 0, 255));
                    for (Statistic value : Statistic.values()) {
                        if (!Config.getConfig().isSet("lb-" + value.getPath() + "-pixels")) continue;
                        String str = String.valueOf((int)value.getForPlayer(ID3));
                        if (value == Statistic.WLR || value == Statistic.KDR) {
                            str = String.valueOf(value.getForPlayer(ID3));
                        }
                        String s2 = Config.getValue("lb-" + value.getPath() + "-pixels");
                        Integer[] ints = LeaderboardTask.getArray(s2);
                        int x = ints[0];
                        int y = ints[1] + yvar * (i + 1);
                        gfx.drawString(str, x, y);
                    }
                    Rank playerRank = Rank.getPlayerRank(ID3);
                    Role r = null;
                    try {
                        r = RBW.mainGuild.getRoleById(playerRank.getId());
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    Color rankColor = r == null ? Color.WHITE : r.getColor();
                    gfx.setColor(rankColor);
                    if (Config.getConfig().isSet("lb-name-pixels")) {
                        Integer[] namePixels = LeaderboardTask.getArray(Config.getConfig().getString("lb-name-pixels"));
                        String ign = Player.getName(ID3);
                        int namepixelsX = namePixels[0];
                        int namepixelsY = namePixels[1] + yvar * (i + 1);
                        gfx.drawString(ign, namepixelsX, namepixelsY);
                    }
                    if (!Config.getConfig().isSet("lb-face-pixels")) continue;
                    Integer[] facePixels = LeaderboardTask.getArray(Config.getConfig().getString("lb-face-pixels"));
                    if (Main.lessCpu) {
                        if (Player.getPlayersCache().containsKey(ID3)) {
                            CachedPlayer cd2 = Player.getPlayersCache().get(ID3);
                            PlayerUUID = cd2.UUID;
                        } else {
                            PlayerUUID = Utils.getUUID(Player.getName(ID3));
                            Player.getPlayersCache().put(ID3, new CachedPlayer(PlayerUUID));
                        }
                    } else {
                        PlayerUUID = Utils.getUUID(Player.getName(ID3));
                    }
                    BufferedImage face = ImageIO.read(new URL("https://visage.surgeplay.com/face/" + Config.getValue("lb-face-size") + "/" + PlayerUUID).openStream());
                    int x = facePixels[0];
                    int y = facePixels[1] + yvar * (i + 1);
                    gfx.drawImage((Image)face, x, y, null);
                }
                gfx.dispose();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write((RenderedImage)lb, "png", stream);
                channel.sendFiles(FileUpload.fromData(stream.toByteArray(), "UPDATE.png")).queue();
            } else {
                Object lb = "";
                for (int i = 0; i < 10; ++i) {
                    if (i >= list.size()) continue;
                    String[] values3 = ((Map.Entry)list.get(i)).toString().split("=");
                    int place = i + 1;
                    String ign = Player.getName(values3[0]);
                    lb = place == 1 ? (String)lb + ":first_place: `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n" : (place == 2 ? (String)lb + ":second_place: `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n" : (place == 3 ? (String)lb + ":third_place: `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n" : (String)lb + "**#" + place + "** `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n"));
                }
                BetterEmbed embed = new BetterEmbed("default", "Elo Leaderboard", "", (String)lb, "Page: 1");
                channel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
            }
        }
    }

    static Integer[] getArray(String s2) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (String s1 : s2.split(",")) {
            ints.add(Integer.parseInt(s1));
        }
        return ints.toArray(new Integer[0]);
    }
}

