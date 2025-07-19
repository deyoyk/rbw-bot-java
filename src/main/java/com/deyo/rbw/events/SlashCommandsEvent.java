/*
 * Recoded by deyo 
 */
package com.deyo.rbw.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.classes.game.Game;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.CommandManager;
import com.deyo.rbw.commands.SlashCommandsManager;
import com.deyo.rbw.commands.types.Command;

public class SlashCommandsEvent
extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        List<String> options;
        super.onSlashCommandInteraction(event);
        TextChannel c = event.getGuild().getTextChannelById(event.getChannel().getId());
        if (c == null) {
            event.getInteraction().reply("You can't send this message here.").setEphemeral(true).queue();
            return;
        }
        String argss = "=" + event.getName().toLowerCase() + " ";
        if (Boolean.parseBoolean(Config.getValue("log-commands"))) {
            System.out.println("[RBW] " + event.getUser().getAsTag() + " used " + event.getName());
        }
        if ((options = SlashCommandsManager.commandOptions.get(event.getName())) == null) {
            event.getInteraction().reply("There was a problem, report this to the developer deyo.").queue();
            return;
        }
        ArrayList<Message.Attachment> attachments = new ArrayList<Message.Attachment>();
        for (String opt : options) {
            OptionMapping option = event.getInteraction().getOption(opt);
            System.out.println("Option: " + opt + " " + option);
            if (option == null) continue;
            try {
                if (option.getType() == OptionType.USER) {
                    argss = argss + option.getAsUser().getId() + " ";
                    continue;
                }
                if (option.getType() == OptionType.STRING) {
                    argss = argss + option.getAsString() + " ";
                    continue;
                }
                if (option.getType() == OptionType.ROLE) {
                    argss = argss + option.getAsRole().getId() + " ";
                    continue;
                }
                if (option.getType() == OptionType.NUMBER) {
                    argss = argss + option.getAsInt() + " ";
                    continue;
                }
                if (option.getType() == OptionType.MENTIONABLE) {
                    argss = argss + option.getAsMentionable().getId() + " ";
                    continue;
                }
                if (option.getType() == OptionType.INTEGER) {
                    argss = argss + option.getAsInt() + " ";
                    continue;
                }
                if (option.getType() == OptionType.CHANNEL) {
                    argss = argss + option.getAsChannel().getId() + " ";
                    continue;
                }
                if (option.getType() == OptionType.BOOLEAN) {
                    argss = argss + option.getAsBoolean() + " ";
                    continue;
                }
                if (option.getType() != OptionType.ATTACHMENT) continue;
                attachments.add(option.getAsAttachment());
            }
            catch (IllegalStateException illegalStateException) {}
        }
        BetterEmbed noPermsEmbed = BetterEmbed.error(Messages.NO_PERMS);
        String[] args2 = argss.split(" ");
        String rawcmd = event.getName().toLowerCase();
        if (rawcmd.equalsIgnoreCase("register")) {
            for (Command command : CommandManager.commands) {
                if (!command.name.equalsIgnoreCase("register")) continue;
                if (CommandManager.checkPerms(command.name, event.getMember(), event.getGuild())) {
                    try {
                        command.command.doCMD(args2, event.getGuild(), event.getMember(), (MessageChannelUnion)((Object)c), new CommandAdapter(event.getInteraction(), attachments), command.usage);
                    }
                    catch (IOException iOException) {}
                } else {
                    event.getInteraction().replyEmbeds(noPermsEmbed.build(), new MessageEmbed[0]).queue();
                }
                break;
            }
        } else if (Player.isPlayer(event.getMember().getId())) {
            if (rawcmd.equalsIgnoreCase("p")) {
                rawcmd = Game.isGameChannel(c.getId()) ? "pick" : "party";
            }
            Command cmd = null;
            for (Command command : CommandManager.commands) {
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
                embed.reply(new CommandAdapter(event.getInteraction(), attachments));
                return;
            }
            if (CommandManager.checkPerms(cmd.name, event.getMember(), event.getGuild())) {
                try {
                    cmd.command.doCMD(args2, event.getGuild(), event.getMember(), (MessageChannelUnion)((Object)c), new CommandAdapter(event.getInteraction(), attachments), cmd.usage);
                }
                catch (IOException iOException) {}
            } else {
                event.getInteraction().replyEmbeds(noPermsEmbed.build(), new MessageEmbed[0]).queue();
            }
        } else {
            BetterEmbed.error(Messages.NOT_REGISTERED).reply(new CommandAdapter(event.getInteraction(), attachments));
        }
    }
}

