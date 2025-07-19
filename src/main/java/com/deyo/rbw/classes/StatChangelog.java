/*
 * Recoded by deyo 
 */
package com.deyo.rbw.classes;

import com.deyo.rbw.commands.types.Statistic;

public class StatChangelog {
    private final String ID;
    private final Statistic statistic;
    private final int change;

    public StatChangelog(String ID2, Statistic statistic, int change) {
        this.ID = ID2;
        this.statistic = statistic;
        this.change = change;
    }

    public void run() {
        this.statistic.setForPlayer(this.ID, this.statistic.getForPlayer(this.ID) + (double)this.change);
    }

    public void revert() {
        this.statistic.setForPlayer(this.ID, this.statistic.getForPlayer(this.ID) - (double)this.change);
    }

    public int getChange() {
        return this.change;
    }

    public Statistic getStatistic() {
        return this.statistic;
    }

    public String getID() {
        return this.ID;
    }
}

