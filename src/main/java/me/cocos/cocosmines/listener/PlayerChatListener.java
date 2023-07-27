package me.cocos.cocosmines.listener;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.event.MineInfoUpdateEvent;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.ModificationService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerChatListener implements Listener {

    private final ModificationService modificationService;

    public PlayerChatListener(ModificationService modificationService) {
        this.modificationService = modificationService;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Notification notification = modificationService.findAction(player.getUniqueId());
        if (notification == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase(LanguageContainer.translate("cancel", String.class))) {
            modificationService.removeAction(player.getUniqueId());
            return;
        }
        notification.getConsumer().accept(event);
    }
}
