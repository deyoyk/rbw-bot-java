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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CachedPlayer;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.Rank;
import com.deyo.rbw.classes.Themes;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.GameState;
import com.deyo.rbw.commands.types.ServerCommand;
import com.deyo.rbw.commands.types.Statistic;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Stats
        implements ServerCommand {
    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) {
        String ID2;
        boolean isFull = false;
        for (String arg : args2) {
            if (!arg.contains("full") && !arg.contains("--text"))
                continue;
            isFull = true;
            break;
        }
        if (args2.length == 1) {
            ID2 = m3.getId();
        } else if (args2[1].equals("full")) {
            ID2 = m3.getId();
        } else {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            ID2 = member.getId();
        }
        if (isFull || !new File("RBW/themes/default.png").exists()) {
            BetterEmbed embed = new BetterEmbed("info", "\uD83D\uDCCA  " + Player.getName(ID2) + "'s Stats", "", "",
                    "");
            embed.addField("__General Stats__", "> `Elo` " + (int) Statistic.ELO.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.ELO) + ")**\n> \u2517 `Peak` "
                    + (int) Statistic.PEAKELO.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.PEAKELO)
                    + ")**\n> `Games` " + (int) Statistic.GAMES.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.GAMES) + ")**\n> `WLR` " + Statistic.WLR.getForPlayer(ID2)
                    + " **(#" + Player.getPlacement(ID2, Statistic.WLR) + ")**\n> `KDR` "
                    + Statistic.KDR.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.KDR)
                    + ")**\n> `Mvp` " + (int) Statistic.MVP.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.MVP) + ")**\n> `Strikes` "
                    + (int) Statistic.STRIKES.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.STRIKES)
                    + ")**\n> `Scored` " + (int) Statistic.SCORED.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.SCORED) + ")**", false);
            embed.addField("__Games Stats__", "> **`Wins`** " + (int) Statistic.WIN.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.WIN) + ")**\n> `Winstreak` "
                    + (int) Statistic.WS.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.WS)
                    + ")**\n> \u2517 `Highest` " + (int) Statistic.HIGHESTWS.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.HIGHESTWS) + ")**\n> **`Losses`** "
                    + (int) Statistic.LOSSES.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.LOSSES)
                    + ")**\n> `Losestreak` " + (int) Statistic.LS.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.LS) + ")**\n> \u2517 `Highest` "
                    + (int) Statistic.HIGHESTLS.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.HIGHESTLS) + ")**\n> **`Kills`** "
                    + (int) Statistic.KILLS.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.KILLS)
                    + ")**\n> **`Deaths`** " + (int) Statistic.DEATHS.getForPlayer(ID2) + " **(#"
                    + Player.getPlacement(ID2, Statistic.DEATHS) + ")**\n> **`Beds`** "
                    + (int) Statistic.BEDS.getForPlayer(ID2) + " **(#" + Player.getPlacement(ID2, Statistic.BEDS)
                    + ")**", false);
            embed.addField("__Other Stats__",
                    "> `Gold` " + (int) Statistic.GOLD.getForPlayer(ID2) + "\n> `Screenshared` "
                            + (int) Statistic.SCREENSHARED.getForPlayer(ID2) + "\n> `Selected theme` "
                            + Player.getTheme(ID2) + "\n> `Owned themes` " + Player.getOwnedThemes(ID2),
                    false);
            embed.reply(msg);
        } else {
            try {
                Integer[] array;
                int variation;
                BufferedImage skin;
                String PlayerUUID;
                Player.fix(ID2, g2);
                Themes theme = Themes.getTheme(Player.getTheme(ID2));
                BufferedImage image = theme.getImage();
                if (Main.lessCpu) {
                    if (Player.getPlayersCache().containsKey(ID2)) {
                        CachedPlayer cd2 = Player.getPlayersCache().get(ID2);
                        PlayerUUID = cd2.UUID;
                    } else {
                        PlayerUUID = Utils.getUUID(Player.getName(ID2));
                        Player.getPlayersCache().put(ID2, new CachedPlayer(PlayerUUID));
                    }
                } else {
                    PlayerUUID = Utils.getUUID(Player.getName(ID2));
                }
                URL url = new URL("https://visage.surgeplay.com/full/" + theme.getConfig().getString("skin-size") + "/"
                        + PlayerUUID);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "RankedBot/1.0 (+https://github.com/SSRequest/RankedBot)");
                try {
                    skin = ImageIO.read(connection.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    BetterEmbed embed = new BetterEmbed("error", "", "", "⚠️ Something went wrong. Please try again.",
                            "");
                    embed.reply(msg);
                    return;
                }
                Graphics2D gfx = (Graphics2D) image.getGraphics();
                Color black = Color.black;
                gfx.setBackground(black);
                gfx.setColor(black);
                Font normal = new Font("Minecraft", 0, theme.getDefaultFontSize());
                Font nameFont = new Font("Minecraft", 0, theme.getNameFontSize());
                gfx.setFont(normal);
                Rank playerRank = Rank.getPlayerRank(ID2);
                Role r = null;
                try {
                    r = g2.getRoleById(playerRank.getId());
                } catch (Exception exception) {
                    // empty catch block
                }
                Color rankColor = r == null ? Color.WHITE : r.getColor();
                String rate = String.valueOf(Statistic.getPercentage(ID2));
                for (Statistic value : Statistic.values()) {
                    String statPath = value.getPath();
                    if (!theme.getConfig().isSet(statPath + "-pixels"))
                        continue;
                    Integer[] array2 = this.getArray(theme.getConfig().getString(statPath + "-pixels"));
                    String str = String.valueOf((int) value.getForPlayer(ID2));
                    if (value == Statistic.WLR || value == Statistic.KDR) {
                        str = String.valueOf(value.getForPlayer(ID2));
                    }
                    if (theme.getThemeFormats().containsKey((Object) value)) {
                        Themes.ThemeFormat format = theme.getThemeFormats().get((Object) value);
                        str = format.format.replaceAll("%v", str).replaceAll("%rate%", rate + "%");
                        gfx.setColor(format.color);
                        gfx.setFont(new Font("Minecraft", 0, format.fontsize));
                    }
                    if (theme.getConfig().isSet(statPath + "-pixels-x")) {
                        int variation2 = theme.getConfig().getInt(statPath + "-pixels-x");
                        if (str.length() > 1) {
                            array2[0] = array2[0] + variation2 * (str.length() - 1);
                        }
                    }
                    gfx.drawString(str, (int) array2[0], (int) array2[1]);
                }
                if (theme.getConfig().isSet("placement-pixels")) {
                    String placement;
                    Integer[] xys = this.getArray(theme.getConfig().getString("placement-pixels"));
                    variation = 0;
                    if (theme.getConfig().isSet("placement-pixels-x")) {
                        variation = theme.getConfig().getInt("placement-pixels-x");
                    }
                    if ((placement = String.valueOf(Player.getPlacement(ID2, Statistic.ELO))).length() > 1) {
                        xys[0] = xys[0] + variation * (placement.length() - 1);
                    }
                    gfx.setFont(normal);
                    gfx.setColor(black);
                    gfx.drawString(placement, (int) xys[0], (int) xys[1]);
                }
                if (theme.getConfig().isSet("recentgames-pixels")) {
                    array = this.getArray(theme.getConfig().getString("recentgames-pixels"));
                    variation = 15;
                    if (theme.getConfig().isSet("recentgames-pixels-y")) {
                        variation = theme.getConfig().getInt("recentgames-pixels-y");
                    }
                    List<Game> playerGames = Game.getPlayerGames(ID2, 6);
                    int y = array[1];
                    int x = array[0];
                    for (Game playerGame : playerGames) {
                        String str = "#" + playerGame.gamenumber;
                        Color color = playerGame.getState() == GameState.SCORED
                                ? (playerGame.getTeam(playerGame.getWinnerteam()).contains(ID2) ? new Color(18, 204, 11)
                                        : new Color(211, 6, 6))
                                : new Color(0, 0, 0);
                        gfx.setColor(color);
                        gfx.drawString(str, x, y);
                        y += variation;
                    }
                    gfx.setColor(new Color(0, 0, 0));
                }
                gfx.setFont(nameFont);
                gfx.setColor(rankColor);
                if (theme.getConfig().isSet("name-pixels")) {
                    Integer[] namePixels = this.getArray(theme.getConfig().getString("name-pixels"));
                    int namepixelsX = namePixels[0];
                    String ign = Player.getName(ID2);
                    if (ign.length() != 16) {
                        int diff = 16 - ign.length();
                        int namePixelsDiff = theme.getConfig().getInt("name-pixels-x");
                        namepixelsX += namePixelsDiff * diff;
                    }
                    int namepixelsY = namePixels[1];
                    gfx.drawString(ign, namepixelsX, namepixelsY);
                }
                if (theme.getConfig().isSet("rank-pixels")) {
                    gfx.setColor(rankColor);
                    Integer[] rankPixels = this.getArray(theme.getConfig().getString("rank-pixels"));
                    String rank = r.getName();
                    gfx.drawString(rank, (int) rankPixels[0], (int) rankPixels[1]);
                }
                if (theme.getConfig().isSet("rankimage-pixels") && playerRank != null && playerRank.rankImage != null) {
                    array = this.getArray(theme.getConfig().getString("rankimage-pixels"));
                    gfx.drawImage(playerRank.rankImage, array[0], (int) array[1], null);
                }
                if (theme.getConfig().isSet("skin-pixels")) {
                    Integer[] skinArray = this.getArray(theme.getConfig().getString("skin-pixels"));
                    gfx.drawImage((Image) skin, skinArray[0], (int) skinArray[1], null);
                }
                gfx.dispose();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write((RenderedImage) image, "png", stream);
                BetterEmbed.replyStats(stream.toByteArray(), Player.getName(ID2) + ".png", msg);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    Integer[] getArray(String s2) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (String s1 : s2.split(",")) {
            ints.add(Integer.parseInt(s1));
        }
        return ints.toArray(new Integer[0]);
    }
}
