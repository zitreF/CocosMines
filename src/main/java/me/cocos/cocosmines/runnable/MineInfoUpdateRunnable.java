package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.event.MineInfoUpdateEvent;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;
import me.cocos.cocosmines.service.HookService;
import me.cocos.gui.helper.ChatHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public final class MineInfoUpdateRunnable implements Runnable {

    private final MineService mineService;
    private final HookService hookService;

    public MineInfoUpdateRunnable(MineService mineService, HookService hookService) {
        this.mineService = mineService;
        this.hookService = hookService;
    }

    @Override
    public void run() {
        for (Mine mine : mineService.getMines()) {
            MineInfoUpdateEvent mineInfoUpdateEvent = new MineInfoUpdateEvent(mine);
            Bukkit.getServer().getPluginManager().callEvent(mineInfoUpdateEvent);
            if (mineInfoUpdateEvent.isCancelled()) {
                continue;
            }
            String timeFormatted = TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis());
            if (hookService.useHologram()) {
                List<String> hologram = new ArrayList<String>(LanguageContainer.translate("hologram-lines", List.class));
                mine.updateHologram(
                        hologram.stream()
                                .map(string -> string
                                        .replace("{NAME}", mine.getName())
                                        .replace("{TIME}", TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis())))
                                .toList()
                );
            }
            if (hookService.useActionbar()) {
                BoundingBox boundingBox = BoundingBox.of(mine.getFirstLocation(), mine.getSecondLocation());
                boundingBox.expand(5);
                String translated = LanguageContainer.translate("actionbar-regeneration", String.class);
                Bukkit.getScheduler().runTask(CocosMines.getInstance(), () -> {
                    for (Entity entity : mine.getWorld().getNearbyEntities(boundingBox, entity -> entity instanceof Player)) {
                        Player player = (Player) entity;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatHelper.colored(translated).replace("{TIME}", timeFormatted)));
                    }
                });
            }
        }
    }
}
