package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.helper.ChatHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public final class MineHologramUpdateRunnable implements Runnable {

    private final MineService mineService;
    private final boolean actionbarNotification;

    public MineHologramUpdateRunnable(MineService mineService, boolean actionbarNotification) {
        this.mineService = mineService;
        this.actionbarNotification = actionbarNotification;
    }

    @Override
    public void run() {
        for (Mine mine : mineService.getMines()) {
            List<String> hologram = new ArrayList<String>(LanguageContainer.translate("hologram-lines", List.class));
            String timeFormatted = TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis());
            mine.updateHologram(
                    hologram.stream()
                            .map(string -> string
                            .replace("{NAME}", mine.getName())
                            .replace("{TIME}", TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis())))
                            .toList()
            );
            if (actionbarNotification) {
                BoundingBox boundingBox = BoundingBox.of(mine.getFirstLocation(), mine.getSecondLocation());
                boundingBox.expand(5);
                Bukkit.getScheduler().runTask(CocosMines.getInstance(), () -> {
                    for (Entity entity : mine.getWorld().getNearbyEntities(boundingBox, entity -> entity instanceof Player)) {
                        Player player = (Player) entity;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatHelper.coloredText("&8● &7Czas do regeneracji&8: &f" + timeFormatted + " &8●")));
                    }
                });
            }
        }
    }
}
