/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class Transcript {
    public static ArrayList<Transcript> transcriptlist = new ArrayList();
    private final String channelID;
    private String transcript = "";
    private HashMap<Long, String> attachments = new HashMap();

    public Transcript(String channelID) {
        this.channelID = channelID;
        transcriptlist.add(this);
    }

    public void updateTranscript(String newmessage) {
        this.transcript = this.transcript + "\n" + newmessage;
    }

    public void updateAttachments(List<Message.Attachment> ats, Member m3) {
        for (Message.Attachment attachment : ats) {
            this.attachments.put(System.currentTimeMillis(), attachment.getUrl());
            this.updateTranscript(m3.getUser().getAsTag() + ":: Sent attachment " + attachment.getUrl());
        }
    }

    public HashMap<Long, String> getAttachments() {
        return this.attachments;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public File toFile() {
        File f = new File("RBW/transcripts/" + this.channelID + ".txt");
        try {
            f.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(f);){
            if (this.transcript.isEmpty()) {
                writer.write("No message was sent in this transcript.");
            } else {
                String[] lines;
                for (String line : lines = this.transcript.split("\n")) {
                    String[] l = line.split("::");
                    if (l.length <= 1) continue;
                    writer.write(l[0] + ": " + l[1] + "\n");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        transcriptlist.remove(this);
        return f;
    }

    public static Transcript getTranscript(String channelID) {
        for (Transcript transcript : transcriptlist) {
            if (!channelID.equalsIgnoreCase(transcript.getChannelID())) continue;
            return transcript;
        }
        return null;
    }
}

