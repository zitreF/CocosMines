package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.runnable.ModificationInfoRunnable;
import me.cocos.cocosmines.service.MineService;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.helper.ChatHelper;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public final class CreateArgument implements Argument {

    private final MineService mineService;
    private final ModificationService modificationService;

    public CreateArgument(MineService mineService, ModificationService modificationService) {
        this.mineService = mineService;
        this.modificationService = modificationService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("correct-usage", String.class) + "&e/cocosmine create" + this.getArguments()));
            return;
        }
        if (!NumberUtils.isDigits(args[2])) {
            player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("correct-usage", String.class) + "&e/cocosmine create" + this.getArguments()));
            return;
        }
        long regenTime = Long.parseLong(args[2]);
        if (regenTime < 1) {
            player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("regeneration-time-error", String.class)));
            return;
        }
        modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-first-location", String.class), chatEvent -> {
            if (!chatEvent.getMessage().equalsIgnoreCase(LanguageContainer.translate("confirm", String.class))) return;
            Location firstBlock = chatEvent.getPlayer().getTargetBlock(Set.of(Material.AIR), 5).getLocation();
            modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-second-location", String.class), chatEvent2 -> {
                if (!chatEvent2.getMessage().equalsIgnoreCase(LanguageContainer.translate("confirm", String.class))) return;
                Location secondBlock = chatEvent.getPlayer().getTargetBlock(Set.of(Material.AIR), 5).getLocation();
                Mine mine = new Mine(args[1], player.getName(), System.currentTimeMillis(), regenTime, Material.BEDROCK, new ArrayList<>(), firstBlock, secondBlock);
                Bukkit.getScheduler().runTask(CocosMines.getInstance(), mine::regenerate);
                mineService.createMine(mine);
                mineService.addMine(mine);
                modificationService.removeAction(player.getUniqueId());
            }));
        }));
        new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
    }

    @Override
    public String getDescription() {
        return LanguageContainer.translate("create-description", String.class);
    }

    @Override
    public String getArguments() {
        return LanguageContainer.translate("create-arguments", String.class);
    }

    @Override
    public String getName() {
        return "create";
    }
}
