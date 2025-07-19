/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes.screenshare;

import java.io.File;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.deyo.rbw.classes.screenshare.ScreenShareStoring;

public class ScreenShare {
    public static ArrayList<ScreenShare> screenShares = new ArrayList();
    private final ScreenShareStoring storing;

    public ScreenShare(ScreenShareStoring storing) {
        this.storing = storing;
        screenShares.add(this);
    }

    public ScreenShare(String channelID, String screenShared) {
        this.storing = new ScreenShareStoring(screenShared);
        this.setChannelID(channelID);
        this.setScreenShared(screenShared);
        screenShares.add(this);
    }

    public static void loadScreenShares() {
        File a = new File("RBW/screenshares");
        if (!a.exists()) {
            return;
        }
        File[] list = a.listFiles();
        if (list == null) {
            System.out.println("ScreenShare is null..");
            return;
        }
        if (list.length > 0) {
            for (File f : list) {
                String screenShared = f.getName().replaceAll(".yml", "");
                new ScreenShare(new ScreenShareStoring(screenShared, f));
            }
            System.out.println("Successfully loaded all screenshares into memory");
        }
    }

    public static void saveData() {
        for (ScreenShare screenShare : screenShares) {
            try {
                screenShare.storing.db.save(screenShare.storing.f);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void delete() {
        this.storing.delete();
        screenShares.remove(this);
    }

    public void setChannelID(String channelID) {
        this.storing.getConfig().set("channel-id", channelID);
    }

    public void setScreenShared(String screenShared) {
        this.storing.getConfig().set("screen-shared", screenShared);
    }

    public void setScreenSharer(String screenSharer) {
        this.storing.getConfig().set("screen-sharer", screenSharer);
    }

    @NotNull
    public String getChannelID() {
        return this.storing.getConfig().getString("channel-id");
    }

    @NotNull
    public String getScreenShared() {
        return this.storing.getConfig().getString("screen-shared");
    }

    @Nullable
    public String getScreenSharer() {
        if (!this.storing.getConfig().isSet("screen-sharer")) {
            return null;
        }
        return this.storing.getConfig().getString("screen-sharer");
    }

    public static ScreenShare getByScreenShared(String screenShared) {
        for (ScreenShare screenShare : screenShares) {
            if (!screenShare.getScreenShared().equalsIgnoreCase(screenShared)) continue;
            return screenShare;
        }
        return null;
    }
}

