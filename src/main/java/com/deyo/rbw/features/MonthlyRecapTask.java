package com.deyo.rbw.features;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;


import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.deyo.rbw.childclasses.BetterEmbed;
import com.deyo.rbw.classes.player.Player;
import com.deyo.rbw.commands.types.Statistic;

/**
 * Once a month (UTC 00:00 on the 1st) this task DM's each registered player a short personalised recap.
 * All heavy DB ops (stats look-ups) are performed lazily per-member to avoid one giant mutex.
 */
public class MonthlyRecapTask implements Runnable {
    private static ScheduledExecutorService scheduler;
    private final JDA jda;

    private MonthlyRecapTask(JDA jda) {
        this.jda = jda;
    }

    /**
     * Start the scheduler – safe to call multiple times.
     */
    public static void init(JDA jda) {
        if (scheduler != null) return;
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "monthly-recap-task");
            t.setDaemon(true);
            return t;
        });
        long delayMs = millisUntilNextFirstOfMonth();
        scheduler.schedule(new MonthlyRecapTask(jda), delayMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        // Send DM to every known player in every guild the bot is in
        jda.getGuilds().forEach(guild -> guild.loadMembers().onSuccess(members -> {
            for (Member member : members) {
                if (member.getUser().isBot()) continue;
                sendRecap(member);
            }
        }));
        // schedule next month
        long delayMs = millisUntilNextFirstOfMonth();
        scheduler.schedule(new MonthlyRecapTask(jda), delayMs, TimeUnit.MILLISECONDS);
    }

    private void sendRecap(Member member) {
        String id = member.getId();
        // Example stats – adapt to real values if different field names exist
        int wins = (int) Statistic.WIN.getForPlayer(id);
        int games = (int) Statistic.GAMES.getForPlayer(id);
        int elo = (int) Statistic.ELO.getForPlayer(id);

        BetterEmbed recap = new BetterEmbed("info",
                "\uD83D\uDCC5  Your Monthly RBW Recap",
                member.getEffectiveAvatarUrl(),
                "Here’s how you performed last month! Keep grinding and aim higher next season.",
                "Ranked BedWars • v" + com.deyo.rbw.Main.version);
        recap.addField("Wins", "`" + wins + "`", true);
        recap.addField("Games Played", "`" + games + "`", true);
        recap.addField("Current ELO", "`" + elo + "`", true);

        member.getUser().openPrivateChannel().flatMap(pc -> pc.sendMessageEmbeds(recap.build()))
                .queue(null, err -> {/* ignore DMs closed */});
    }

    private static long millisUntilNextFirstOfMonth() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextFirst = now.withDayOfMonth(1).plusMonths(1).truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        return Duration.between(now, nextFirst).toMillis();
    }
}
