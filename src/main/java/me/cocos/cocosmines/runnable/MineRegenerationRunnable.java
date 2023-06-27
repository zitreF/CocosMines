package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.data.Mine;

public final class MineRegenerationRunnable implements Runnable {

    private final Mine mine;

    public MineRegenerationRunnable(Mine mine) {
        this.mine = mine;
    }

    @Override
    public void run() {
        mine.regenerate();
    }
}
