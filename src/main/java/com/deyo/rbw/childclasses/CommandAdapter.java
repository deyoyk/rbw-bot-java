/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses;

import java.util.List;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class CommandAdapter {
    private final boolean message;
    private final Object msg;
    private final List<Message.Attachment> attachments;

    public CommandAdapter(Message message) {
        this.message = true;
        this.msg = message;
        this.attachments = message.getAttachments();
    }

    public Object getMsg() {
        return this.msg;
    }

    public boolean isMessage() {
        return this.message;
    }

    public CommandAdapter(SlashCommandInteraction interaction, List<Message.Attachment> attachments) {
        this.message = false;
        this.msg = interaction;
        this.attachments = attachments;
    }

    public List<Message.Attachment> getAttachments() {
        return this.attachments;
    }
}

