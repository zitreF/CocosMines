package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.event.MineRegenerationEvent;
import me.cocos.cocosmines.service.MineService;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public final class MineRegenerationRunnable implements Runnable {

    private final MineService mineService;

    public MineRegenerationRunnable(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void run() {
        for (Mine mine : mineService.getMines()) {
            if (mine.getLastRegenerationTime() < System.currentTimeMillis()) {
                mine.setLastRegenerationTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(mine.getRegenTime()));
                CocosMines.getInstance().getMineQueueRunnable().queue(mine);
            }
        }
    }
}
