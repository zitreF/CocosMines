package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;

import java.util.ArrayList;
import java.util.List;

public final class MineHologramUpdateRunnable implements Runnable {

    private final MineService mineService;

    public MineHologramUpdateRunnable(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void run() {
        for (Mine mine : mineService.getMines()) {
            List<String> hologram = new ArrayList<String>(LanguageContainer.translate("hologram-lines", List.class));
            mine.updateHologram(
                    hologram.stream()
                            .map(string -> string
                            .replace("{NAME}", mine.getName())
                            .replace("{TIME}", TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis())))
                            .toList()
            );
        }
    }
}
