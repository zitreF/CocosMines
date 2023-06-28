package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.service.MineService;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public final class MineHologramUpdateRunnable implements Runnable {

    private final MineService mineService;

    public MineHologramUpdateRunnable(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void run() {
        for (Mine mine : mineService.getMines()) {
            mine.updateHologram(List.of(
                    "&8● &a&lGenerator " + mine.getName() + " &8●",
                    "",
                    "&a⌚ &fCzas do regeneracji: &a" + TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis())
            ));
        }
    }
}
