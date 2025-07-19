/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses;

import com.deyo.rbw.childclasses.Msg;

public enum Messages {
    GAME_ALREADY_STARTED("game-started"),
    NOT_CAPTAIN("not-captain"),
    PICKED_YOURSELF("picked-yourself"),
    QUEUE_INVALID_PLAYERS("queue-invalid-players"),
    QUEUE_ALREADY_EXISTS("queue-already-exists"),
    QUEUE_INVALID_MODE("queue-invalid-mode"),
    QUEUE_CASUAL("queue-casual"),
    QUEUE_DELETED("queue-deleted"),
    NOT_BANNED("player-not-banned"),
    RANK_DELETED("rank-deleted"),
    INVALID_RANK("rank-doesnt-exist"),
    NO_RANKS("no-ranks"),
    NOT_SUBMITTED("not-submitted"),
    CASUAL_GAME("casual-game"),
    NOT_SCORED("not-scored"),
    SUCCESS_WIPED("successfully-wiped"),
    SS_SELF("ss-self"),
    THEME_ALREADY_EXISTS("theme-already-exists"),
    INVALID_TIME_FORMAT("invalid-time-format"),
    ALREADY_BANNED("already-banned"),
    SUCCESS_REGISTER("successfully-registered"),
    IGN_TOO_SHORT("ign-too-short"),
    IGN_TOO_LONG("ign-too-long"),
    ALREADY_REGISTERED("already-registered"),
    NOT_GAMECHANNEL("not-game-channel"),
    INVALID_QUEUE("invalid-queue"),
    INVALID_GAME("invalid-game"),
    INVALID_VC("invalid-vc"),
    INVALID_ROLE("invalid-role"),
    INVALID_PLAYER("invalid-player"),
    WRONG_USAGE("wrong-usage"),
    NO_PERMS("no-perms"),
    NOT_REGISTERED("not-registered"),
    COMMAND_NOT_FOUND("command-not-found");

    private final String path;

    private Messages(String path) {
        this.path = path;
    }

    public String get() {
        return Msg.getMsg(this.path);
    }

    public String getPath() {
        return this.path;
    }
}

