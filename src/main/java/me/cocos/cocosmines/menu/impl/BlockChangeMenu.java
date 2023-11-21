package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.helper.MaterialHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.runnable.ModificationInfoRunnable;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.gui.builder.gui.GuiBuilder;
import me.cocos.gui.builder.item.impl.ItemBuilder;
import me.cocos.gui.data.GuiItem;
import me.cocos.gui.gui.Gui;
import me.cocos.gui.gui.holder.GuiHolder;
import me.cocos.gui.helper.ChatHelper;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public final class BlockChangeMenu {

    private final Gui gui;
    private final Mine mine;
    private final ModificationService modificationService;

    public BlockChangeMenu(Mine mine) {
        this.gui = GuiBuilder.normal(LanguageContainer.translate("edit-change-blocks", String.class), 3)
                .disposable(true)
                .blockPlayerInventory(false)
                .build();
        this.mine = mine;
        this.modificationService = CocosMines.getInstance().getModificationService();
        this.update();
        this.gui.setOnClick((event, player) -> {
            event.setCancelled(true);
            if (!(event.getClickedInventory() instanceof PlayerInventory)) {
                return;
            }
            ItemStack clickedBlock = event.getCurrentItem();
            if (clickedBlock == null || !MaterialHelper.isValid(clickedBlock.getType())) {
                return;
            }
            MineBlock mineBlock = new MineBlock(50, clickedBlock.getType());
            mine.addSpawningBlock(mineBlock);
            int firstEmpty = this.gui.getInventory().firstEmpty();
            if (firstEmpty == -1) return;
            List<String> blockInfoLore = new ArrayList<String>(LanguageContainer.translate("block-action-lore", List.class));
            GuiItem guiItem = ItemBuilder.from(mineBlock.getMaterial())
                    .lore(
                            blockInfoLore.stream()
                                    .map(string -> string.replace("{CHANCE}", String.valueOf(mineBlock.getChance())))
                                    .toList()
                    )
                    .asGuiItem()
                    .onClick((e, p) -> {
                        this.onBlockClick(e, firstEmpty, mineBlock, p);
                    });
            this.gui.setItem(firstEmpty, guiItem);
        });
    }

    private void update() {
        this.gui.getInventory().clear();
        for (MineBlock mineBlock : mine.getSpawningBlocks()) {
            int firstEmpty = this.gui.getInventory().firstEmpty();
            if (firstEmpty == -1) return;
            List<String> blockInfoLore = new ArrayList<String>(LanguageContainer.translate("block-action-lore", List.class));
            GuiItem guiItem = ItemBuilder.from(mineBlock.getMaterial())
                    .lore(
                            blockInfoLore.stream()
                                    .map(string -> string.replace("{CHANCE}", String.valueOf(mineBlock.getChance())))
                                    .toList()
                    )
                    .asGuiItem()
                    .onClick((event, player) -> {
                        this.onBlockClick(event, firstEmpty, mineBlock, player);
                    });
            this.gui.setItem(firstEmpty, guiItem);
        }
    }

    private void onBlockClick(InventoryClickEvent event, int firstEmpty, MineBlock mineBlock, Player player) {
        if (event.getClickedInventory() == null || !(event.getClickedInventory().getHolder() instanceof GuiHolder)) return;
        if (event.isLeftClick()) {
            this.gui.setItem(firstEmpty, GuiItem.of(Material.AIR));
            mine.removeSpawningBlock(mineBlock);
        } else if (event.isRightClick()) {
            player.closeInventory();
            modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-percentage-set", String.class), chatEvent -> {
                if (!NumberUtils.isDigits(chatEvent.getMessage())) {
                    player.sendMessage(ChatHelper.colored(LanguageContainer.translate("must-be-number", String.class)));
                    return;
                }
                double percent = Double.parseDouble(chatEvent.getMessage());
                if (percent > 100 || percent < 0) {
                    player.sendMessage(ChatHelper.colored(LanguageContainer.translate("percent-error", String.class)));
                    return;
                }
                mine.updateMineBlockChance(mineBlock, percent);
                modificationService.removeAction(player.getUniqueId());
                BlockChangeMenu blockChangeMenu = new BlockChangeMenu(mine);
                Bukkit.getScheduler().runTask(CocosMines.getInstance(), () -> blockChangeMenu.open(player));
            }));
            new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
        }
    }

    public void open(Player player) {
        this.gui.open(player);
    }
}
