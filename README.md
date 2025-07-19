# RBW Bot / Plugin

Java-based Discord **and** Spigot plugin that powers the Ranked Bedwars (RBW) ecosystem.
It handles everything from queues, scoring, bans & strikes, maps, themes and even on-demand screen-share sessions – all tightly integrated with Discord via JDA.

---

## Features

* **Game management** – create / wipe games, automatic team assignment, live logs
* **Scoring system** – win / lose / undo, MVP, elo decay & leaderboard updates
* **Queue system** – dynamically manage Bedwars queues from Discord
* **Player utilities** – force-register, prefixes, nicknames, guild handling
* **Moderation** – bans, strikes, screen-share freeze & accept / deny workflow
* **Themes & maps** – full CRUD commands and in-game placeholders
* **Rich embeds** – consistent responses through `BetterEmbed`

All of the above are implemented as slash commands / prefix commands contained in `src/main/java/com/deyo/rbw/commands`.

---

## Tech stack

| Purpose                | Library                                | Version |
|------------------------|----------------------------------------|---------|
| Discord integration    | [JDA](https://github.com/DV8FromTheWorld/JDA) | 5.0.0-beta.9 |
| Minecraft plugin API   | Spigot 1.8.8 (BedWars1058 hook)        | snapshot |
| Configuration & utils  | SnakeYAML, Commons-IO, Guava, JSON-Simple |
| Build tool             | Maven (Java 11)                        |         |

---

## Building

```bash
# clone the repository
$ git clone https://github.com/deyoerr/RBW-Bot/rbw-bot.git
$ cd rbw

# build a shaded jar with all dependencies
$ mvn clean package
```
The shaded jar will appear at `target/RBWBott-<version>-jar-with-dependencies.jar`.

---

## Installation

1. **Discord Bot** – create an application on the Discord Developer Portal, enable privileged intents, then copy the bot token into your `config.yml` / `config.json`.
2. **Spigot / Paper server** – drop the generated jar into the `plugins/` directory and start the server. The plugin will bootstrap both the Discord bot and Bukkit listeners.

Make sure you supply all required IDs in the config (guild, channels, roles etc.) – see the keys referenced in `Config.java`.

---

## Running locally

```bash
# from the root of the project
$ java -jar target/RBWBott-1.5.0-jar-with-dependencies.jar
```

On a production Minecraft server just restart / reload plugins as usual (`/reload` or full restart).

---

## Contributing

Pull requests are welcome! Please open an issue first to discuss the change you wish to make.

When contributing:
1. Follow the existing code style.
2. Run `mvn spotless:apply` (if configured) before committing.
3. Test both Discord and in-game functionality where applicable.

---

## License

This project is released under the MIT License – see `LICENSE` for details.

---

## Credits

Recoded by **deyo** and **deyo**.
Originally authored by **kdc**.
