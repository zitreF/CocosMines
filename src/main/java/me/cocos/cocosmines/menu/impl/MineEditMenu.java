package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.runnable.ModificationInfoRunnable;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.builder.impl.MenuBuilder;
import me.cocos.menu.helper.ChatHelper;
import me.cocos.menu.helper.GuiHelper;
import me.cocos.menu.type.MenuType;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class MineEditMenu extends Menu {

    public MineEditMenu(Mine mine) {
        super(LanguageContainer.translate("editing", String.class) + mine.getName(), 3, true);
        ModificationService modificationService = CocosMines.getInstance().getModificationService();
        this.setOnInventoryClick(((event, player) -> event.setCancelled(true)));
        GuiHelper.border(this.getInventory(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        ItemStack name = ItemBuilder.from(Material.NAME_TAG).withItemName(LanguageContainer.translate("edit-change-name", String.class)).build();
        ItemStack icon = ItemBuilder.from(Material.EMERALD_ORE).withItemName(LanguageContainer.translate("edit-change-icon", String.class)).build();
        ItemStack time = ItemBuilder.from(Material.CLOCK).withItemName(LanguageContainer.translate("edit-change-time-regeneration", String.class)).build();
        ItemStack blocks = ItemBuilder.from(Material.GRASS_BLOCK).withItemName(LanguageContainer.translate("edit-change-blocks", String.class)).build();
        ItemStack coords = ItemBuilder.from(Material.COMPASS).withItemName(LanguageContainer.translate("edit-change-location", String.class)).build();
        ItemStack reset = ItemBuilder.from(Material.ANVIL).withItemName(LanguageContainer.translate("edit-reset-mine", String.class)).build();
        ItemStack teleport = ItemBuilder.from(Material.ENDER_PEARL).withItemName(LanguageContainer.translate("edit-teleport", String.class)).build();
        this.setItem(name, 10).onInventoryClick((event, player) -> {
            player.closeInventory();
            modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-change-name", String.class), chatEvent -> {
                String message = chatEvent.getMessage();
                mine.setName(message);
                modificationService.removeAction(player.getUniqueId());
            }));
            new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
        });
        this.setItem(icon, 11).onInventoryClick((event, player) -> {
            player.closeInventory();
            Menu menu = MenuBuilder.from(MenuType.SIMPLE, LanguageContainer.translate("edit-change-icon", String.class), 1, false)
                    .blockPlayerInventory(false)
                    .build();
            menu.setOnInventoryClose((event2, player2) -> {
                mine.setLogo(menu.getInventory().getItem(0).getType());
                menu.dispose();
            });
            player.openInventory(menu.getInventory());
        });
        this.setItem(blocks, 12).onInventoryClick((event, player) -> {
            player.closeInventory();

            BlockChangeMenu blockChangeMenu = new BlockChangeMenu(mine);

            player.openInventory(blockChangeMenu.getInventory());
        });
        this.setItem(coords, 13).onInventoryClick((event, player) -> {
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
        this.setItem(reset, 14).onInventoryClick((event, player) -> {
            player.closeInventory();
            mine.regenerate();
            mine.setLastRegenerationTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(mine.getRegenTime()));
        });
        this.setItem(teleport, 15).onInventoryClick((event, player) -> {
            player.closeInventory();
            Location first = mine.getFirstLocation().clone();
            Location second = mine.getSecondLocation().clone();
            int bonusY = second.getBlockY()-first.getBlockY();
            player.teleport(first.add(second).multiply(1/2d).add(0, bonusY/2d, 0));
        });
        this.setItem(time, 16).onInventoryClick((event, player) -> {
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

    }
}
