/*
 * Recoded by deyo 
 */
package com.deyo.rbw.events;

import java.util.List;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.Perms;
import com.deyo.rbw.commands.CommandManager;
import com.deyo.rbw.commands.types.Command;
import com.deyo.rbw.commands.types.Statistic;

public class HelpEvent
extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        super.onStringSelectInteraction(event);
        StringBuilder availableStats = new StringBuilder();
        int maxv = Statistic.values().length - 1;
        for (int i = 0; i < Statistic.values().length; ++i) {
            Statistic value = Statistic.values()[i];
            availableStats.append("`" + value.getPath() + "`");
            if (i != maxv) availableStats.append("/");
        }
        if (event.getComponentId().equals("help-" + event.getMember().getId())) {
            BetterEmbed ab;
            String selection = event.getValues().get(0);
            if (selection.equalsIgnoreCase("player")) {
                ab = new BetterEmbed("default", "Players Commands", "", "Here are all the commands that Players have.", "");
                for (Command command : CommandManager.commands) {
                    List<String> perms;
                    if (command.type != Command.CommandType.PLAYER) continue;
                    String description = "- " + command.description;
                    String perm = Perms.getDb().getString(command.name, "");
                    if (!perm.equalsIgnoreCase("everyone") && (perms = (List<String>)Perms.groups.getOrDefault(perm, null)) != null) {
                        description = description + "\nPermission: ";
                        for (String s2 : perms) {
                            description = description + "<@&" + s2 + ">";
                        }
                    }
                    ab.addField(command.usage, description, false);
                }
            } else if (selection.equalsIgnoreCase("scorers")) {
                ab = new BetterEmbed("default", "Scorers Commands", "", "Here are all the commands that Scorers have.", "");
                for (Command command : CommandManager.commands) {
                    List<String> perms;
                    if (command.type != Command.CommandType.SCORERS) continue;
                    String description = "- " + command.description;
                    String perm = Perms.getDb().getString(command.name, "");
                    if (!perm.equalsIgnoreCase("everyone") && (perms = (List<String>)Perms.groups.getOrDefault(perm, null)) != null) {
                        description = description + "\nPermission: ";
                        for (String s3 : perms) {
                            description = description + "<@&" + s3 + ">";
                        }
                    }
                    ab.addField(command.usage, description, false);
                }
            } else if (selection.equalsIgnoreCase("admins") && CommandManager.hasGroup("admin", event.getMember(), event.getGuild())) {
                ab = new BetterEmbed("default", "Administrators Help", "", "Here are all the Admin commands.", "");
                for (Command command : CommandManager.commands) {
                    List<String> perms;
                    if (command.type != Command.CommandType.ADMINS) continue;
                    String description = "- " + command.description;
                    String perm = Perms.getDb().getString(command.name, "");
                    if (!perm.equalsIgnoreCase("everyone") && (perms = (List<String>)Perms.groups.getOrDefault(perm, null)) != null) {
                        description = description + "\nPermission: ";
                        for (String s4 : perms) {
                            description = description + "<@&" + s4 + ">";
                        }
                    }
                    ab.addField(command.usage, description, false);
                }
            } else if (selection.equalsIgnoreCase("staff") && CommandManager.hasGroup("staff", event.getMember(), event.getGuild())) {
                ab = new BetterEmbed("default", "Staff Commands", "", "Here are all the commands that Staff have.", "");
                for (Command command : CommandManager.commands) {
                    List<String> perms;
                    if (command.type != Command.CommandType.STAFF) continue;
                    String description = "- " + command.description;
                    String perm = Perms.getDb().getString(command.name, "");
                    if (!perm.equalsIgnoreCase("everyone") && (perms = (List<String>)Perms.groups.getOrDefault(perm, null)) != null) {
                        description = description + "\nPermission: ";
                        for (String s5 : perms) {
                            description = description + "<@&" + s5 + ">";
                        }
                    }
                    ab.addField(command.usage, description, false);
                }
            } else if (selection.equalsIgnoreCase("owners") && CommandManager.hasGroup("owner", event.getMember(), event.getGuild())) {
                ab = new BetterEmbed("default", "Owners Commands", "", "Here are all the Owner commands.", "");
                for (Command command : CommandManager.commands) {
                    List<String> perms;
                    if (command.type != Command.CommandType.OWNERS) continue;
                    String description = "- " + command.description;
                    String perm = Perms.getDb().getString(command.name, "");
                    if (!perm.equalsIgnoreCase("everyone") && (perms = (List<String>)Perms.groups.getOrDefault(perm, null)) != null) {
                        description = description + "\nPermission: ";
                        for (String s6 : perms) {
                            description = description + "<@&" + s6 + ">";
                        }
                    }
                    ab.addField(command.usage, description, false);
                }
            } else if (selection.equalsIgnoreCase("screenshares")) {
                ab = new BetterEmbed("default", "Screensharers Commands", "", "Here are all the Screensharer commands.", "");
                for (Command command : CommandManager.commands) {
                    List<String> perms;
                    if (command.type != Command.CommandType.SCREENSHARERS) continue;
                    String description = "- " + command.description;
                    String perm = Perms.getDb().getString(command.name, "");
                    if (!perm.equalsIgnoreCase("everyone") && (perms = (List<String>)Perms.groups.getOrDefault(perm, null)) != null) {
                        description = description + "\nPermission: ";
                        for (String s7 : perms) {
                            description = description + "<@&" + s7 + ">";
                        }
                    }
                    ab.addField(command.usage, description, false);
                }
            } else {
                ab = new BetterEmbed("error", "No permission", "", "# :x: You do not have permission to view that.", "");
            }
            event.editMessageEmbeds(ab.build()).queue();
        }
    }
}

