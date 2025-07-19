/*
 * Recoded by deyo 
 */
package com.deyo.rbw.commands.types;

import java.util.List;

import com.deyo.rbw.commands.types.ServerCommand;

public class Command {
    public String name;
    public String description;
    public String usage;
    public CommandType type;
    public ServerCommand command;
    public List<String> aliases;

    public Command(String name, ServerCommand command, CommandType type, String description, String usage, String ... aliases) {
        this.name = name;
        this.command = command;
        this.type = type;
        this.description = description;
        this.usage = usage;
        this.aliases = List.of(aliases);
    }

    public static enum CommandType {
        PLAYER,
        SCORERS,
        ADMINS,
        STAFF,
        OWNERS,
        SCREENSHARERS;

    }
}

