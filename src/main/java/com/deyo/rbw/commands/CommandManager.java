/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands;

import java.io.IOException;
import java.util.ArrayList;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.Perms;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Transcript;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.commands.Help;
import com.deyo.rbw.commands.commands.ReloadConfig;
import com.deyo.rbw.commands.commands.SaveData;
import com.deyo.rbw.commands.commands.admin.GetPlayerFile;
import com.deyo.rbw.commands.commands.admin.GetQueueFile;
import com.deyo.rbw.commands.commands.admin.GetRankFile;
import com.deyo.rbw.commands.commands.admin.SetPlayerFile;
import com.deyo.rbw.commands.commands.admin.SetQueueFile;
import com.deyo.rbw.commands.commands.admin.SetRankFile;
import com.deyo.rbw.commands.commands.banningsystem.Ban;
import com.deyo.rbw.commands.commands.banningsystem.BanInfo;
import com.deyo.rbw.commands.commands.banningsystem.Bans;
import com.deyo.rbw.commands.commands.banningsystem.Strike;
import com.deyo.rbw.commands.commands.banningsystem.StrikeRequest;
import com.deyo.rbw.commands.commands.banningsystem.UnStrike;
import com.deyo.rbw.commands.commands.banningsystem.Unban;
import com.deyo.rbw.commands.commands.gamesystem.Call;
import com.deyo.rbw.commands.commands.gamesystem.CallRemove;
import com.deyo.rbw.commands.commands.gamesystem.ClearGame;
import com.deyo.rbw.commands.commands.gamesystem.GameInfo;
import com.deyo.rbw.commands.commands.gamesystem.Games;
import com.deyo.rbw.commands.commands.gamesystem.Pick;
import com.deyo.rbw.commands.commands.gamesystem.Queue;
import com.deyo.rbw.commands.commands.gamesystem.QueueStats;
import com.deyo.rbw.commands.commands.gamesystem.Void;
import com.deyo.rbw.commands.commands.gamesystem.WipeGames;
import com.deyo.rbw.commands.commands.mapsystem.Maps;
import com.deyo.rbw.commands.commands.player.ClaimElo;
import com.deyo.rbw.commands.commands.player.Fix;
import com.deyo.rbw.commands.commands.player.ForceRegister;
import com.deyo.rbw.commands.commands.player.ForceRename;
import com.deyo.rbw.commands.commands.player.LeaderBoard;
import com.deyo.rbw.commands.commands.player.List;
import com.deyo.rbw.commands.commands.player.Nick;
import com.deyo.rbw.commands.commands.player.Party;
import com.deyo.rbw.commands.commands.player.PrefixToggle;
import com.deyo.rbw.commands.commands.player.Register;
import com.deyo.rbw.commands.commands.player.RemoveNick;
import com.deyo.rbw.commands.commands.player.Rename;
import com.deyo.rbw.commands.commands.player.Stats;
import com.deyo.rbw.commands.commands.player.party.PartyListCMD;
import com.deyo.rbw.commands.commands.queuesystem.AddQueue;
import com.deyo.rbw.commands.commands.queuesystem.DeleteQueue;
import com.deyo.rbw.commands.commands.queuesystem.Queues;
import com.deyo.rbw.commands.commands.ranksystem.AddRank;
import com.deyo.rbw.commands.commands.ranksystem.DeleteRank;
import com.deyo.rbw.commands.commands.ranksystem.ModifyRank;
import com.deyo.rbw.commands.commands.ranksystem.Ranks;
import com.deyo.rbw.commands.commands.scoringsystem.Lose;
import com.deyo.rbw.commands.commands.scoringsystem.Modify;
import com.deyo.rbw.commands.commands.scoringsystem.Mvp;
import com.deyo.rbw.commands.commands.scoringsystem.Score;
import com.deyo.rbw.commands.commands.scoringsystem.UndoGame;
import com.deyo.rbw.commands.commands.scoringsystem.Win;
import com.deyo.rbw.commands.commands.scoringsystem.Wipe;
import com.deyo.rbw.commands.commands.screensharingsystem.CloseSS;
import com.deyo.rbw.commands.commands.screensharingsystem.Freeze;
import com.deyo.rbw.commands.commands.screensharingsystem.SS;
import com.deyo.rbw.commands.commands.screensharingsystem.Unfreeze;
import com.deyo.rbw.commands.commands.themessystem.Theme;
import com.deyo.rbw.commands.types.Command;

import java.util.concurrent.TimeUnit;

public class CommandManager
extends ListenerAdapter {
    public static ArrayList<Command> commands = new ArrayList();
    public static String prefix;
    BetterEmbed noPermsEmbed = new BetterEmbed("error", "No Permission", "", Messages.NO_PERMS.get(), "");
    BetterEmbed blockedChannelEmbed = null;

    public CommandManager() {
        commands.add(new Command("register", new Register(), Command.CommandType.PLAYER, "Link your Minecraft account to a ranked profile using a nickname. Required to track ELO and stats.", "=register (nick)", "link"));
        commands.add(new Command("rename", new Rename(), Command.CommandType.PLAYER, "Change your registered nickname.", "=rename (newNick)", "relink"));
        commands.add(new Command("nick", new Nick(), Command.CommandType.PLAYER, "Set a public nickname. Use =nick only to revert to your username.", "=nick (nick or clear to remove)", "nickname"));
        commands.add(new Command("theme", new Theme(), Command.CommandType.PLAYER, "Theme system.", "=theme [cmds]", "themesystem", "th"));
        commands.add(new Command("removenick", new RemoveNick(), Command.CommandType.PLAYER, "Remove your custom nickname and restore your original username.", "=removenick", "rn"));
        commands.add(new Command("stats", new Stats(), Command.CommandType.PLAYER, "View your or another player\u2019s ELO, win/loss ratio, bed breaks, and MVP history.", "=stats [player]", "s", "i", "info"));
        commands.add(new Command("baninfo", new BanInfo(), Command.CommandType.PLAYER, "View information about a ban of a player.", "=baninfo (player)", "history", "bi", "punishments", "pun"));
        commands.add(new Command("strikerequest", new StrikeRequest(), Command.CommandType.PLAYER, "Strike request a player.", "=strikerequest (player) (reason)", "sr", "strikereq"));
        commands.add(new Command("pick", new Pick(), Command.CommandType.PLAYER, "Pick a player in a game.", "=pick (player)", "p"));
        commands.add(new Command("leaderboard", new LeaderBoard(), Command.CommandType.PLAYER, "Browse the global ELO leaderboard. Filter by rank tier or page through results.", "=leaderboard [type] [page]", "lb"));
        commands.add(new Command("queues", new Queues(), Command.CommandType.PLAYER, "List all active matchmaking queues with team sizes and modes (e.g., 2v2, 4v4).", "=queues", "queueslist"));
        commands.add(new Command("queue", new Queue(), Command.CommandType.PLAYER, "Check your current queue status, including estimated wait time.", "=queue", "q"));
        commands.add(new Command("queuestats", new QueueStats(), Command.CommandType.PLAYER, "Check your current queue stats.", "=queuestats", "qs"));
        commands.add(new Command("party", new Party(), Command.CommandType.PLAYER, "Manage parties: invite players, promote leaders, kick members, or disband the group.", "=party <invite [@player] / promote [@player] / kick [@player] / join [@player] / disband / list / leave / help / warp>", new String[0]));
        commands.add(new Command("void", new Void(), Command.CommandType.PLAYER, "Request to void a game in progress (requires majority vote in the team).", "=void", "voidgame", "v"));
        commands.add(new Command("call", new Call(), Command.CommandType.PLAYER, "Invite a player to your private voice channel during a match.", "=call (player)", "c"));
        commands.add(new Command("callremove", new CallRemove(), Command.CommandType.PLAYER, "Remove a player from your private voice channel.", "=callremove (player)", "cr", "delcall"));
        commands.add(new Command("prefixtoggle", new PrefixToggle(), Command.CommandType.PLAYER, "Toggle the prefix before the name.", "=prefixtoggle", "pt", "toggleprefix"));
        commands.add(new Command("fix", new Fix(), Command.CommandType.PLAYER, "Refresh your data if stats or ranks aren\u2019t updating correctly.", "=fix (player)", "rme", "refreshuser", "refresh"));
        commands.add(new Command("maps", new Maps(), Command.CommandType.PLAYER, "View all loaded Ranked BedWars maps. Filter by type (e.g., 2v2, 4v4).", "=maps [type]", new String[0]));
        commands.add(new Command("help", new Help(), Command.CommandType.PLAYER, "Get detailed explanations for commands or list all available actions.", "=help [command]", new String[0]));
        commands.add(new Command("list", new List(), Command.CommandType.PLAYER, "List all online players in the server.", "=list", "listplayers", "playerslist"));
        commands.add(new Command("ranks", new Ranks(), Command.CommandType.PLAYER, "Shows the current elo system.", "=ranks", "elotable", "eloranks"));
        commands.add(new Command("pl", new PartyListCMD(), Command.CommandType.PLAYER, "Alias for =party list", "=pl (player)", "partylist"));
        commands.add(new Command("claimelo", new ClaimElo(), Command.CommandType.PLAYER, "Claims your startup elo", "=claimelo", "eloclaim"));
        commands.add(new Command("ban", new Ban(), Command.CommandType.STAFF, "Ban a player temporarily (e.g., 7d, 1mo). Logs reason and duration in the moderation database.", "=ban <ID/mention> <duration> <reason>", new String[0]));
        commands.add(new Command("unban", new Unban(), Command.CommandType.STAFF, "Remove a player\u2019s active ban. Requires approval from a senior moderator.", "=unban (player)", "removeban", "remban", "delban"));
        commands.add(new Command("bans", new Bans(), Command.CommandType.STAFF, "List all currently banned players with durations and reasons.", "=bans", "banlist"));
        commands.add(new Command("strike", new Strike(), Command.CommandType.STAFF, "Issue a strike for rule violations. Accumulated strikes trigger automated bans.", "=strike (player) [reason]", "addstrike"));
        commands.add(new Command("unstrike", new UnStrike(), Command.CommandType.STAFF, "Remove a strike from a player\u2019s record. Requires audit log justification.", "=unstrike (player)", "removestrike", "remstrike", "delstrike"));
        commands.add(new Command("cleargame", new ClearGame(), Command.CommandType.STAFF, "Force-end a corrupted or stuck game.", "=cleargame (gameID)", "cg", "clear", "forcevoid", "fv"));
        commands.add(new Command("games", new Games(), Command.CommandType.STAFF, "View active/past games. Filter by player, status (playing/scored/voided), or game ID.", "=games (player) (playing/scored/voided)", "gs", "gamesall"));
        commands.add(new Command("modify", new Modify(), Command.CommandType.STAFF, "Edit a player\u2019s ELO, wins, losses, or bed breaks. Requires senior staff approval.", "=modify (player) [field] [value]", "edit", "modifystats"));
        commands.add(new Command("forceregister", new ForceRegister(), Command.CommandType.STAFF, "Manually link a player\u2019s account to a nickname.", "=forceregister (player) (nick)", "fr", "freg"));
        commands.add(new Command("forcerename", new ForceRename(), Command.CommandType.STAFF, "Force-change a player\u2019s registered nickname (e.g., for inappropriate names).", "=forcerename (player) (newNick)", "fren", "forceren"));
        commands.add(new Command("addrank", new AddRank(), Command.CommandType.ADMINS, "Create a new rank tier with custom ELO thresholds, rewards, and decay rules.", "=addrank <role ID/mention> <starting elo> <ending elo> <win elo> <lose elo> <mvp elo> <bed elo> <decay elo *optional*>", "createrank"));
        commands.add(new Command("deleterank", new DeleteRank(), Command.CommandType.ADMINS, "Delete a rank from the elo system.", "=deleterank (rank)", "removerank", "delrank"));
        commands.add(new Command("modifyrank", new ModifyRank(), Command.CommandType.ADMINS, "Adjust ELO values for a rank (e.g., MVP bonus, decay rate).", "=modifyrank (rank) (type: mvp/win/lose/decay/bed) (amount)", "editrank"));
        commands.add(new Command("addqueue", new AddQueue(), Command.CommandType.ADMINS, "Create a new matchmaking queue (e.g., 2v2 Casual, 4v4 Ranked).", "=addqueue <ID> <playersInTeam> <pickingMode> <true/false (is the q casual)>", "createqueue"));
        commands.add(new Command("deletequeue", new DeleteQueue(), Command.CommandType.ADMINS, "Remove a queue. Active players in this queue will be kicked.", "=deletequeue (queueID)", "delqueue", "remqueue"));
        commands.add(new Command("reloadconfig", new ReloadConfig(), Command.CommandType.ADMINS, "Reload configuration files (ranks, queues, maps) without restarting the server.", "=reloadconfig", "reload", "rc"));
        commands.add(new Command("savedata", new SaveData(), Command.CommandType.ADMINS, "Force an immediate save of all player/game data to disk.", "=savedata", new String[0]));
        commands.add(new Command("getqueuefile", new GetQueueFile(), Command.CommandType.OWNERS, "Retrieve a queue file (YAML) for debugging.", "=getqueuefile (queue)", "getqfile"));
        commands.add(new Command("setqueuefile", new SetQueueFile(), Command.CommandType.OWNERS, "Overwrite a queue file (use with caution).", "=setqueuefile (queue)", "setqfile"));
        commands.add(new Command("getplayerfile", new GetPlayerFile(), Command.CommandType.OWNERS, "Retrieve a player\u2019s raw data file (JSON/YAML) for debugging.", "=getplayerfile (player)", "getfile"));
        commands.add(new Command("setplayerfile", new SetPlayerFile(), Command.CommandType.OWNERS, "Overwrite a player\u2019s data file (use with caution).", "=setplayerfile (player)", "setfile"));
        commands.add(new Command("getrankfile", new GetRankFile(), Command.CommandType.OWNERS, "Access the configuration file for a specific rank.", "=getrankfile (rank)", new String[0]));
        commands.add(new Command("setrankfile", new SetRankFile(), Command.CommandType.OWNERS, "Replace a rank\u2019s configuration file.", "=setrankfile (rank) (file)", new String[0]));
        commands.add(new Command("wipegames", new WipeGames(), Command.CommandType.OWNERS, "Permanently delete all game records from the database.", "=wipegames", new String[0]));
        commands.add(new Command("wipe", new Wipe(), Command.CommandType.OWNERS, "Reset all stats for a player (or everyone). Irreversible action.", "=wipe (player or everyone)", "reset", "wipestats"));
        commands.add(new Command("freeze", new Freeze(), Command.CommandType.SCREENSHARERS, "Freeze a player during a screenshare investigation. Blocks movement and actions.", "=freeze (player)", new String[0]));
        commands.add(new Command("unfreeze", new Unfreeze(), Command.CommandType.SCREENSHARERS, "Unfreeze a player after a screenshare is completed.", "=unfreeze (player)", "removefreeze", "remfreeze"));
        commands.add(new Command("closess", new CloseSS(), Command.CommandType.SCREENSHARERS, "Terminate an active screenshare session and log the results.", "=closess", "ssclose"));
        commands.add(new Command("ss", new SS(), Command.CommandType.SCREENSHARERS, "Initiate a screenshare session for a suspected rule violator.", "=ss (player)", "screenshare"));
        commands.add(new Command("score", new Score(), Command.CommandType.SCORERS, "Manually resolve a disputed game. Specify winning team, MVP (or 'none'), and bed status.", "=score <number> <team> <mvp ID/mention>\nif theres no mvp write \"none\" instead", "game"));
        commands.add(new Command("undogame", new UndoGame(), Command.CommandType.SCORERS, "Revert a scored game and restore previous stats. Requires game ID.", "=undogame (gameID)", "undo"));
        commands.add(new Command("gameinfo", new GameInfo(), Command.CommandType.SCORERS, "View detailed information about a game, including teams and ELO changes.", "=gameinfo (gameID)", "gi", "viewgame"));
        commands.add(new Command("win", new Win(), Command.CommandType.SCORERS, "Manually add wins to a player\u2019s record (e.g., for tournament results).", "=win (player) (times)", new String[0]));
        commands.add(new Command("lose", new Lose(), Command.CommandType.SCORERS, "Manually add losses to a player\u2019s record.", "=lose (player) (times)", new String[0]));
        commands.add(new Command("mvp", new Mvp(), Command.CommandType.SCORERS, "Award MVP status to a player for a game.", "=mvp (player) (times)", "givemvp", "addmvp"));
        boolean changed = false;
        for (Command command : commands) {
            if (Perms.getDb().isSet(command.name)) continue;
            changed = true;
            Perms.getDb().set(command.name, "everyone");
        }
        if (changed) {
            Perms.save();
        }
        // Set up blockedChannelEmbed with commands channel mention
        String commandsChannelId = Config.getConfig().getString("commandschannel");
        String channelMention = commandsChannelId != null ? " <#" + commandsChannelId + ">" : "";
        blockedChannelEmbed = new BetterEmbed("error", "Channel Blocked", "", "Commands are blocked in this channel. Please use the commands channel" + channelMention + ".", "");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args2 = event.getMessage().getContentRaw().split(" ");
        Guild g2 = event.getGuild();
        Member m3 = event.getMember();
        if (m3 == null) {
            return;
        }
        MessageChannelUnion c = event.getChannel();
        Message msg = event.getMessage();
        Transcript t = Transcript.getTranscript(c.getId());
        if (t != null) {
            if (!msg.getAttachments().isEmpty()) {
                t.updateAttachments(msg.getAttachments(), m3);
            }
            t.updateTranscript(m3.getUser().getName() + "::" + msg.getContentRaw());
        }
        if (msg.getContentRaw().startsWith(prefix)) {
            java.util.List blockedChannelsRaw = Config.getConfig().getStringList("blockedcommands");
            java.util.List<String> blockedChannels = null;
            if (blockedChannelsRaw != null) {
                blockedChannels = new java.util.ArrayList<>();
                for (Object o : blockedChannelsRaw) {
                    blockedChannels.add(String.valueOf(o));
                }
            }
            if (blockedChannels != null && blockedChannels.contains(c.getId())) {
                msg.replyEmbeds(blockedChannelEmbed.build()).queue(message -> {
                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                });
                return;
            }
            
            if (Config.getConfig().getBoolean("log-commands")) {
                System.out.println("[RBW] " + m3.getUser().getName() + " used " + msg.getContentRaw());
            }
            if (RBW.commandEmoji != null) {
                event.getMessage().addReaction(RBW.commandEmoji).queue();
            }
            if (args2[0].equalsIgnoreCase(prefix + "register")) {
                for (Command command : commands) {
                    if (!command.name.equalsIgnoreCase("register")) continue;
                    if (CommandManager.checkPerms(command.name, m3, g2)) {
                        try {
                            command.command.doCMD(args2, g2, m3, c, new CommandAdapter(msg), command.usage);
                        }
                        catch (IOException iOException) {}
                    } else {
                        msg.replyEmbeds(this.noPermsEmbed.build(), new MessageEmbed[0]).queue();
                    }
                    break;
                }
            } else if (Player.isPlayer(m3.getId())) {
                String rawcmd = args2[0].toLowerCase().split(prefix)[1];
                if (rawcmd.equalsIgnoreCase("p")) {
                    rawcmd = Game.isGameChannel(c.getId()) ? "pick" : "party";
                }
                Command cmd = null;
                for (Command command : commands) {
                    if (command.aliases.contains(rawcmd)) {
                        cmd = command;
                        break;
                    }
                    if (!command.name.equalsIgnoreCase(rawcmd)) continue;
                    cmd = command;
                    break;
                }
                if (cmd == null) {
                    BetterEmbed embed = BetterEmbed.error(Messages.COMMAND_NOT_FOUND);
                    embed.reply(msg);
                    return;
                }
                if (CommandManager.checkPerms(cmd.name, m3, g2)) {
                    try {
                        cmd.command.doCMD(args2, g2, m3, c, new CommandAdapter(msg), cmd.usage);
                    }
                    catch (IOException iOException) {}
                } else {
                    msg.replyEmbeds(this.noPermsEmbed.build(), new MessageEmbed[0]).queue();
                }
            } else {
                BetterEmbed.error(Messages.NOT_REGISTERED).reply(msg);
            }
        }
    }

    public static boolean checkPerms(String cmd, Member m3, Guild g2) {
        String perm = Perms.getDb().getString(cmd, null);
        if (perm == null) {
            return true;
        }
        if (perm.equals("everyone")) {
            return true;
        }
        java.util.List<String> perms = (java.util.List<String>) Perms.groups.getOrDefault(perm, null);
        if (perms == null) {
            System.out.println("Please configure a valid group for command " + cmd);
            return true;
        }
        if (m3.hasPermission(Permission.ADMINISTRATOR) && !cmd.equalsIgnoreCase("wipe")) {
            return true;
        }
        return CommandManager.hasGroup(perm, m3, g2);
    }

    public static boolean hasGroup(String group, Member m3, Guild g2) {
        java.util.List<String> perms = (java.util.List<String>) Perms.groups.getOrDefault(group, null);
        if (m3.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        }
        java.util.List<Role> roles = m3.getRoles();
        if (perms == null) return false;
        for (String r : perms) {
            Role role = g2.getRoleById(r);
            if (role == null || !roles.contains(role)) continue;
            return true;
        }
        return false;
    }
}

