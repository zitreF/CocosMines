package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.data.Mine;

import java.util.concurrent.TimeUnit;

public final class MineRegenerationRunnable implements Runnable {

    private final Mine mine;

    public MineRegenerationRunnable(Mine mine) {
        this.mine = mine;
    }

    @Override
    public void run() {
        mine.setLastRegenerationTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(mine.getRegenTime()));
        mine.regenerate();
    }
}
