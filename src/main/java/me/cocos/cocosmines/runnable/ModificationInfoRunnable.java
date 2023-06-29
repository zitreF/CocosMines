package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.helper.ChatHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class ModificationInfoRunnable extends BukkitRunnable {

    private final ModificationService modificationService;
    private final Player player;

    public ModificationInfoRunnable(ModificationService modificationService, Player player) {
        this.modificationService = modificationService;
        this.player = player;
    }

    @Override
    public void run() {
        if (!modificationService.contains(player.getUniqueId())) {
            this.cancel();
            return;
        }
        Notification notification = modificationService.findAction(player.getUniqueId());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatHelper.coloredText("&8● &7Wpisz &c\"Anuluj\" &7aby anulowac &8●")));
        player.sendTitle(ChatHelper.coloredText("&8● &e&lCOCOS&6&lMINES &8●"), ChatHelper.coloredText(notification.getNotification()), 0, 30, 5);
    }
}