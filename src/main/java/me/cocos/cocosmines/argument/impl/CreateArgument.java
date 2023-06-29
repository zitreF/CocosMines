package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Notification;
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
            player.sendMessage(ChatHelper.coloredText("&8>> &7Poprawne uzycie&8: &e/cocosmine create <nazwa> <czas-regeneracji>"));
            return;
        }
        if (!NumberUtils.isNumber(args[2])) {
            player.sendMessage(ChatHelper.coloredText("&8>> &7Poprawne uzycie&8: &e/cocosmine create <nazwa> <czas-regeneracji>"));
            return;
        }
        long regenTime = Long.parseLong(args[2]);
        if (regenTime < 1) {
            player.sendMessage(ChatHelper.coloredText("&8>> &7Czas regeneracji nie moze byc mniejszy niz 1!"));
            return;
        }
        modificationService.addAction(player.getUniqueId(), new Notification("&7Napisz &a\"potwierdz\" &7aby potwierdzic pierwsza lokalizacje", chatEvent -> {
            if (chatEvent.getMessage().equalsIgnoreCase("anuluj")) {
                modificationService.removeAction(player.getUniqueId());
                return;
            }
            if (!chatEvent.getMessage().equalsIgnoreCase("potwierdz")) return;
            Location firstBlock = chatEvent.getPlayer().getTargetBlock(Set.of(Material.AIR), 5).getLocation();
            modificationService.addAction(player.getUniqueId(), new Notification("&7Napisz &a\"potwierdz\" &7aby potwierdzic druga lokalizacje", chatEvent2 -> {
                if (chatEvent2.getMessage().equalsIgnoreCase("anuluj")) {
                    modificationService.removeAction(player.getUniqueId());
                    return;
                }
                if (!chatEvent2.getMessage().equalsIgnoreCase("potwierdz")) return;
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
        return "tworzy kopalnie";
    }

    @Override
    public String getArguments() {
        return " <nazwa> <czas-regeneracji>";
    }

    @Override
    public String getName() {
        return "create";
    }
}
