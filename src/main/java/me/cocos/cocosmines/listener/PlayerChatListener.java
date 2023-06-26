package me.cocos.cocosmines.listener;

import me.cocos.cocosmines.service.ModificationService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public final class PlayerChatListener implements Listener {

    private final ModificationService modificationService;

    public PlayerChatListener(ModificationService modificationService) {
        this.modificationService = modificationService;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Consumer<AsyncPlayerChatEvent> callback = modificationService.findAction(player.getUniqueId());
        if (callback == null) {
            return;
        }
        event.setCancelled(true);
        callback.accept(event);
    }
}
