/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.commands.screensharingsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Messages;
import com.deyo.rbw.childclasses.RBW;
import com.deyo.rbw.classes.Utils;
import com.deyo.rbw.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Freeze
implements ServerCommand {
    private static List<Button> buttons(String target) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(Button.secondary("Accept-SS::" + target, "\u2705"));
        buttons.add(Button.secondary("Deny-SS::" + target, "\u274c"));
        return buttons;
    }

    @Override
    public void doCMD(String[] args2, Guild g2, Member m3, MessageChannelUnion c, CommandAdapter msg, String usage) throws IOException {
        if (args2.length >= 2) {
            Member member = Utils.getArg(args2[1], g2);
            if (member == null) {
                BetterEmbed.error(Messages.INVALID_PLAYER).reply(msg);
                return;
            }
            Role freezeRole = RBW.frozenRole;
            if (freezeRole == null) {
                BetterEmbed error = new BetterEmbed("error", "Critical Error", "", "Report this to the owner (FREEZE_ROLE_NOT_SET)", "");
                error.reply(msg);
                return;
            }
            if (member.getRoles().contains(freezeRole)) {
                BetterEmbed error = new BetterEmbed("error", "", "", "That player is already frozen (Has " + freezeRole.getAsMention() + " role)", "");
                error.reply(msg);
                return;
            }
            g2.addRoleToMember(member, freezeRole).queue();
            BetterEmbed embed = new BetterEmbed("warning", "\u26A0\uFE0F  Screen-Share Instructions", "", member.getAsMention() + " You are now **frozen**.\n\nYou shall **not log off**. Head to <https://anydesk.com/> and prepare to share your screen.\nYou have **7-10 minutes**.\n\nIf you would like to admit, now is your chance.\n\nIf you are caught cheating without admitting, or if illegal modifications are found within a week, or you delete files / unplug USB devices, you will be punished.\n\nYour timer starts **now**.", "");
            embed.reply(msg, member.getAsMention());
        } else {
            BetterEmbed error = new BetterEmbed("error", "", "", Messages.WRONG_USAGE.get().replaceAll("%usage%", usage), "");
            error.reply(msg);
        }
    }
}

