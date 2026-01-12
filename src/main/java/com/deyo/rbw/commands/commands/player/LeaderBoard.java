/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.player;

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
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CachedPlayer;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class LeaderBoard
implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length <= 3) {
            HashMap<String, Double> unsortedMap = new HashMap<String, Double>();
            String stat = args2.length > 1 ? args2[1] : "elo";
            int page = args2.length == 3 ? Integer.parseInt(args2[2]) : 1;
            Statistic statistic = Statistic.getFromString(stat);
            if (statistic == null) {
                Object s2 = "";
                int maxv = Statistic.values().length - 1;
                for (int i = 0; i < Statistic.values().length; ++i) {
                    Statistic value = Statistic.values()[i];
                    s2 = i == maxv ? (String)s2 + "`" + value.getPath() + "`" : (String)s2 + "`" + value.getPath() + "`/";
                }
                BetterEmbed error = new BetterEmbed("error", "", "", "🚫 Unknown statistic. Valid options: " + (String)s2, "");
                error.reply(msg);
                return;
            }
            DecimalFormat formatter = statistic == Statistic.WLR || statistic == Statistic.KDR ? new DecimalFormat("#0.00") : new DecimalFormat("#0");
            for (String ID2 : Player.getPlayers().keySet()) {
                unsortedMap.put(ID2, statistic.getForPlayer(ID2));
            }
            Map<String, Double> sortedMap = Utils.sortByValue(unsortedMap);
            LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(sortedMap.entrySet());
            if (page * 10 > list.size() + 10 || page == 0) {
                BetterEmbed embed = new BetterEmbed("error", "", "", "🚫 That page doesn’t exist.", "");
                embed.reply(msg);
                return;
            }
            File lbImage = new File("RBW/leaderboard.png");
            if (statistic == Statistic.ELO && lbImage.exists()) {
                BufferedImage lb = ImageIO.read(lbImage);
                Graphics2D gfx = (Graphics2D)lb.getGraphics();
                gfx.setBackground(Color.BLACK);
                gfx.setColor(Color.BLACK);
                gfx.setFont(new Font("Minecraft", 0, Config.getConfig().getInt("lb-fontsize")));
                for (int i = page - 1; i < 10; ++i) {
                    String PlayerUUID;
                    if (i >= list.size()) continue;
                    String[] values2 = ((Map.Entry)list.get(i)).toString().split("=");
                    String ID3 = values2[0];
                    int yvar = Config.getConfig().getInt("lb-y-variation");
                    gfx.setColor(new Color(204, 122, 0, 255));
                    for (Statistic value : Statistic.values()) {
                        if (!Config.getConfig().isSet("lb-" + value.getPath() + "-pixels")) continue;
                        String str = String.valueOf((int)value.getForPlayer(ID3));
                        if (value == Statistic.WLR) {
                            str = String.valueOf(value.getForPlayer(ID3));
                        }
                        String s3 = Config.getValue("lb-" + value.getPath() + "-pixels");
                        Integer[] ints = this.getArray(s3);
                        int x = ints[0];
                        int y = ints[1] + yvar * (i + 1);
                        gfx.drawString(str, x, y);
                    }
                    Rank playerRank = Rank.getPlayerRank(ID3);
                    Role r = null;
                    try {
                        r = g2.getRoleById(playerRank.getId());
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    Color rankColor = r == null ? Color.WHITE : r.getColor();
                    gfx.setColor(rankColor);
                    if (Config.getConfig().isSet("lb-name-pixels")) {
                        Integer[] namePixels = this.getArray(Config.getConfig().getString("lb-name-pixels"));
                        String ign = Player.getName(ID3);
                        int namepixelsX = namePixels[0];
                        int namepixelsY = namePixels[1] + yvar * (i + 1);
                        gfx.drawString(ign, namepixelsX, namepixelsY);
                    }
                    if (!Config.getConfig().isSet("lb-face-pixels")) continue;
                    Integer[] facePixels = this.getArray(Config.getConfig().getString("lb-face-pixels"));
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
                BetterEmbed.replyStats(stream.toByteArray(), Player.getName(m3.getId()) + ".png", msg);
            } else {
                Object lb = "";
                for (int i = (page - 1) * 10; i < page * 10; ++i) {
                    if (i >= list.size()) continue;
                    String[] values3 = ((Map.Entry)list.get(i)).toString().split("=");
                    int place = i + 1;
                    String ign = Player.getName(values3[0]);
                    lb = place == 1 ? (String)lb + ":first_place: `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n" : (place == 2 ? (String)lb + ":second_place: `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n" : (place == 3 ? (String)lb + ":third_place: `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n" : (String)lb + "**#" + place + "** `" + ign + "` \u2014 " + formatter.format(Double.parseDouble(values3[1])) + "\n"));
                }
                BetterEmbed embed = new BetterEmbed("info", "\uD83C\uDFC6  " + Utils.formatName(stat) + " Leaderboard", "", (String)lb, "Page **" + page + "**");
                embed.replyWithButtons(LeaderBoard.buttons(m3.getId()), msg);
            }
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }

    private static List<Button> buttons(String ID2) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(Button.secondary("previousLB-" + ID2, "\u2190"));
        buttons.add(Button.secondary("nextLB-" + ID2, "\u2192"));
        return buttons;
    }

    Integer[] getArray(String s2) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (String s1 : s2.split(",")) {
            ints.add(Integer.parseInt(s1));
        }
        return ints.toArray(new Integer[0]);
    }
}

