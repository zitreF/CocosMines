package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.runnable.ModificationInfoRunnable;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.gui.builder.gui.GuiBuilder;
import me.cocos.gui.builder.item.impl.ItemBuilder;
import me.cocos.gui.data.GuiItem;
import me.cocos.gui.gui.Gui;
import me.cocos.gui.helper.ChatHelper;
import me.cocos.gui.pattern.Pattern;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class MineEditMenu {

    private final Gui gui;

    public MineEditMenu(Mine mine) {
        this.gui = GuiBuilder.normal(LanguageContainer.translate("editing", String.class) + mine.getName(), 3)
                .disposable(true)
                .blockPlayerInventory(true)
                .onClick((event, player) -> event.setCancelled(true))
                .pattern(Pattern.BORDER, GuiItem.of(Material.GRAY_STAINED_GLASS_PANE))
                .build();
        ModificationService modificationService = CocosMines.getInstance().getModificationService();
        GuiItem name = ItemBuilder.from(Material.NAME_TAG)
                .name(LanguageContainer.translate("edit-change-name", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();
                    modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-change-name", String.class), chatEvent -> {
                        String message = chatEvent.getMessage();
                        mine.setName(message);
                        modificationService.removeAction(player.getUniqueId());
                    }));
                    new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
                });
        GuiItem icon = ItemBuilder.from(Material.EMERALD_ORE)
                .name(LanguageContainer.translate("edit-change-icon", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();
                    Gui logoGui = GuiBuilder.normal(LanguageContainer.translate("edit-change-icon", String.class), 1)
                            .disposable(false)
                            .blockPlayerInventory(false)
                            .build();
                    logoGui.setOnClose(((closeEvent, closePlayer) -> {
                        mine.setLogo(logoGui.getInventory().getItem(0).getType());
                        logoGui.dispose();
                    }));
                    logoGui.open(player);
                });
        GuiItem blocks = ItemBuilder.from(Material.GRASS_BLOCK)
                .name(LanguageContainer.translate("edit-change-blocks", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();

                    BlockChangeMenu blockChangeMenu = new BlockChangeMenu(mine);
                    blockChangeMenu.open(player);
                });
        GuiItem coords = ItemBuilder.from(Material.COMPASS)
                .name(LanguageContainer.translate("edit-change-location", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();
                    modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-first-location", String.class), chatEvent -> {
                        if (!chatEvent.getMessage().equalsIgnoreCase(LanguageContainer.translate("confirm", String.class))) return;
                        Location firstBlock = chatEvent.getPlayer().getTargetBlock(Set.of(Material.AIR), 5).getLocation();
                        modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-second-location", String.class), chatEvent2 -> {
                            if (!chatEvent2.getMessage().equalsIgnoreCase(LanguageContainer.translate("confirm", String.class))) return;
                            Location secondBlock = chatEvent.getPlayer().getTargetBlock(Set.of(Material.AIR), 5).getLocation();
                            mine.updateLocation(firstBlock, secondBlock);
                            modificationService.removeAction(player.getUniqueId());
                        }));
                    }));
                    new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
                });
        GuiItem reset = ItemBuilder.from(Material.ANVIL)
                .name(LanguageContainer.translate("edit-reset-mine", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();
                    mine.regenerate();
                    mine.setLastRegenerationTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(mine.getRegenTime()));
                });
        GuiItem teleport = ItemBuilder.from(Material.ENDER_PEARL)
                .name(LanguageContainer.translate("edit-teleport", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();
                    Location first = mine.getFirstLocation().clone();
                    Location second = mine.getSecondLocation().clone();
                    int bonusY = second.getBlockY()-first.getBlockY();
                    player.teleport(first.add(second).multiply(1/2d).add(0, bonusY/2d, 0));
                });
        GuiItem time = ItemBuilder.from(Material.CLOCK)
                .name(LanguageContainer.translate("edit-change-time-regeneration", String.class))
                .asGuiItem()
                .onClick((event, player) -> {
                    player.closeInventory();
                    modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-time-regeneration", String.class), chatEvent -> {
                        if (!NumberUtils.isDigits(chatEvent.getMessage())) {
                            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("must-be-number", String.class)));
                            return;
                        }
                        long regenTime = Long.parseLong(chatEvent.getMessage());
                        if (regenTime < 1) {
                            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("regeneration-time-error", String.class)));
                            return;
                        }
                        mine.setRegenTime(regenTime);
                        modificationService.removeAction(player.getUniqueId());
                    }));
                    new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
                });
        this.gui.setItem(10, name);
        this.gui.setItem(11, icon);
        this.gui.setItem(12, blocks);
        this.gui.setItem(13, coords);
        this.gui.setItem(14, reset);
        this.gui.setItem(15, teleport);
        this.gui.setItem(16, time);
    }

    public void open(Player player) {
        this.gui.open(player);
    }
}
