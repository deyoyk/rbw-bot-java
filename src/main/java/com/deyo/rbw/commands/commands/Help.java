/*
 * Recoded by deyo – simplified and modernised
 */
package com.deyo.rbw.commands.commands;

import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class Help
                implements ServerCommand {
        @Override
        public void doCMD(String[] args, Guild guild, Member member, MessageChannelUnion ch, CommandAdapter msg,
                        String usage) {

                BetterEmbed embed = new BetterEmbed("info", "\uD83D\uDCDD  ASRBW Bot Help",
                                guild.getIconUrl() == null ? "" : guild.getIconUrl(),
                                "Choose a category below to view its commands. \n \n * **Made by - Dreamrela** \n * **Designed by Veysure & EsDeeBot**",
                                "Made by - ASRBW Development Team");

                // Quick overview fields

                StringSelectMenu menu = StringSelectMenu.create("help-" + member.getId())
                                .setPlaceholder("Select command group...")
                                .addOptions(
                                                SelectOption.of("Player Commands", "player")
                                                                .withDescription("General player utilities")
                                                                .withEmoji(Emoji.fromUnicode("\uD83C\uDFAE")),
                                                SelectOption.of("Scorers Commands", "scorers")
                                                                .withDescription("Scorer-specific commands")
                                                                .withEmoji(Emoji.fromUnicode("\uD83D\uDECF")),
                                                SelectOption.of("Screensharers Commands", "screenshares")
                                                                .withDescription("Screenshare tools")
                                                                .withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")),
                                                SelectOption.of("Staff Commands", "staff")
                                                                .withDescription("Moderation utilities")
                                                                .withEmoji(Emoji.fromUnicode("\uD83D\uDEE0")),
                                                SelectOption.of("Admin Commands", "admins")
                                                                .withDescription("Admin-only commands")
                                                                .withEmoji(Emoji.fromUnicode("\uD83D\uDEE1")),
                                                SelectOption.of("Owners Commands", "owners")
                                                                .withDescription("Owner-level commands")
                                                                .withEmoji(Emoji.fromUnicode("\uD83D\uDC51")))
                                .build();
                embed.replyActionRows(msg, List.of(menu));
        }
}
