/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import com.deyo.rbw.Main;
import com.deyo.rbw.childclasses.CommandAdapter;
import com.deyo.rbw.childclasses.Config;
import com.deyo.rbw.childclasses.Messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;

public class BetterEmbed {
    // Emoji prefixes for quick visual context
    private static final String SUCCESS_EMOJI = "\u2705 ";   // check
    private static final String ERROR_EMOJI   = "\u274C ";   // cross
    private static final String INFO_EMOJI    = "\u2139 ";   // info
    private static final String WARNING_EMOJI = "\u26A0 ";   // warn
    EmbedBuilder eb = new EmbedBuilder();
    private final String type;
    private static Color defaultColor = new Color(0x5865F2); // Discord blurple fallback
    private static Color infoColor = new Color(0x5865F2);
    private static Color warningColor = new Color(0xFAA61A);
    private static Color successColor = new Color(0x57F287);
    private static Color errorColor = new Color(0xED4245);



    public static BetterEmbed error(Messages message) {
        return new BetterEmbed("error", "Error", "", message.get(), "");
    }

    public static void loadColors() {
        // Allows dynamic override via config but keeps safe defaults
        try {
        String[] d = Config.getValue("default").split(",");
        String[] s2 = Config.getValue("success").split(",");
        String[] e = Config.getValue("error").split(",");
        defaultColor = new Color(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        successColor = new Color(Integer.parseInt(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2]));
        errorColor = new Color(Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2]));
            infoColor = defaultColor;  // keep for clarity
        } catch (Exception ex) {
            // If config missing or malformed we keep the hard-coded defaults
        }
    }

    public BetterEmbed(String type, String title, String thumbnailURL, String description, String footer) {
        this.type = type;
        // Pick colour & emoji based on type
        String emojiPrefix = "";
        if (type.equalsIgnoreCase("success")) {
            this.eb.setColor(successColor);
            emojiPrefix = SUCCESS_EMOJI;
                } else if (type.equalsIgnoreCase("error")) {
                        this.eb.setColor(errorColor);
            emojiPrefix = ERROR_EMOJI;
                } else if (type.equalsIgnoreCase("warning")) {
            this.eb.setColor(warningColor);
            emojiPrefix = WARNING_EMOJI;
        } else {
                        this.eb.setColor(infoColor);
        }
        if (Config.getValue("footer") != null) {
            this.eb.setFooter(Config.getValue("footer").replaceAll("%name%", Config.getValue("server-name")).replaceAll("%version%", Main.version), Config.getValue("footer-image"));
            this.eb.setTimestamp(Instant.now());
        }
        if (!title.equalsIgnoreCase("")) {
            // prepend emoji if not already present
            if (!title.startsWith(":")) {
                title = emojiPrefix + title;
            }
            this.eb.setTitle(title);
        }
        if (!description.equalsIgnoreCase("")) {
            this.eb.setDescription(description);
        }
        if (!thumbnailURL.equalsIgnoreCase("")) {
            this.eb.setThumbnail(thumbnailURL);
        }
        if (!footer.equalsIgnoreCase("")) {
            this.eb.setFooter(footer);
        }
    }

    public void addField(String title, String content, boolean inline) {
        this.eb.addField(title, content, inline);
    }

    public void setDescription(String description) {
        this.eb.setDescription(description);
    }

    public void setImage(String url) {
        this.eb.setImage(url);
    }

        /*
     * Convenience factory methods to reduce boilerplate
     */
    public static BetterEmbed success(String title, String description) {
        return new BetterEmbed("success", title, "", description, "");
    }
    public static BetterEmbed info(String title, String description) {
        return new BetterEmbed("info", title, "", description, "");
    }
    public static BetterEmbed warning(String title, String description) {
        return new BetterEmbed("warning", title, "", description, "");
    }
    public MessageEmbed build() {
        return this.eb.build();
    }

    public void reply(Message msg) {
        msg.replyEmbeds(this.eb.build(), new MessageEmbed[0]).queue();
    }

    public void reply(CommandAdapter msg) {
        if (msg.getMsg() instanceof Message) {
            ((Message)msg.getMsg()).replyEmbeds(this.eb.build(), new MessageEmbed[0]).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            if (this.type.equalsIgnoreCase("error")) {
                e.replyEmbeds(this.eb.build(), new MessageEmbed[0]).setEphemeral(true).queue();
                return;
            }
            e.replyEmbeds(this.eb.build(), new MessageEmbed[0]).queue();
        }
    }

    public void reply(CommandAdapter msg, String replyContent) {
        if (msg.getMsg() instanceof Message) {
            ((MessageCreateAction)((Message)msg.getMsg()).reply(replyContent).setEmbeds(this.eb.build())).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            if (this.type.equalsIgnoreCase("error")) {
                ((ReplyCallbackAction)e.reply(replyContent).addEmbeds(this.eb.build())).setEphemeral(true).queue();
                return;
            }
            e.replyEmbeds(this.eb.build(), new MessageEmbed[0]).queue();
        }
    }

    public void replyActionRows(CommandAdapter msg, Collection<ItemComponent> actionRow) {
        if (msg.getMsg() instanceof Message) {
            ((MessageCreateAction)((Message)msg.getMsg()).replyEmbeds(this.eb.build(), new MessageEmbed[0]).setActionRow(actionRow)).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            ((ReplyCallbackAction)e.replyEmbeds(this.eb.build(), new MessageEmbed[0]).setActionRow(actionRow)).queue();
        }
    }

    public static void reply(String s2, CommandAdapter msg) {
        if (msg.getMsg() instanceof Message) {
            ((Message)msg.getMsg()).reply(s2).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            e.reply(s2).queue();
        }
    }

    public static void reply(File s2, CommandAdapter msg) {
        if (msg.getMsg() instanceof Message) {
            ((Message)msg.getMsg()).replyFiles(FileUpload.fromData(s2)).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            ((ReplyCallbackAction)e.reply("** **").addFiles(FileUpload.fromData(s2))).queue();
        }
    }

    public static void replyStats(byte[] data, String d, CommandAdapter msg) {
        if (msg.getMsg() instanceof Message) {
            ((MessageCreateAction)((Message)msg.getMsg()).reply("** **").addFiles(FileUpload.fromData(data, d))).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            ((ReplyCallbackAction)e.reply("** **").addFiles(FileUpload.fromData(data, d))).queue();
        }
    }

    public void replyWithButtons(List<Button> buttons, CommandAdapter msg) {
        if (msg.getMsg() instanceof Message) {
            ((MessageCreateAction)((Message)msg.getMsg()).replyEmbeds(this.build(), new MessageEmbed[0]).setActionRow(buttons)).queue();
        }
        if (msg.getMsg() instanceof SlashCommandInteraction) {
            SlashCommandInteraction e = (SlashCommandInteraction)msg.getMsg();
            ((ReplyCallbackAction)e.replyEmbeds(this.build(), new MessageEmbed[0]).addActionRow(buttons)).queue();
        }
    }

    public static List<Message.Attachment> getAttachments(CommandAdapter msg) {
        return msg.getAttachments();
    }
}

