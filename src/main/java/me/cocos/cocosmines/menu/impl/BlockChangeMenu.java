package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.helper.MaterialHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.runnable.ModificationInfoRunnable;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.helper.ChatHelper;
import me.cocos.menu.holder.MenuHolder;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class BlockChangeMenu extends Menu {

    private final Mine mine;
    private final ModificationService modificationService;

    public BlockChangeMenu(Mine mine) {
        super(LanguageContainer.translate("edit-change-blocks", String.class), 3);
        this.mine = mine;
        this.modificationService = CocosMines.getInstance().getModificationService();
        this.setBlockPlayerInventory(false);
        this.update();
        this.setOnInventoryClick((event, player) -> {
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
            int firstEmpty = this.getInventory().firstEmpty();
            if (firstEmpty == -1) return;
            List<String> blockInfoLore = new ArrayList<String>(LanguageContainer.translate("block-action-lore", List.class));
            ItemBuilder builder = ItemBuilder.from(mineBlock.getMaterial())
                    .withLore(
                            blockInfoLore.stream()
                                    .map(string -> string.replace("{CHANCE}", String.valueOf(mineBlock.getChance())))
                                    .toList()
                    );
            this.setItem(builder.build(), firstEmpty).onInventoryClick((e, p) -> {
                this.onBlockClick(e, firstEmpty, mineBlock, p);
            });
        });
        this.setOnInventoryClose((event, player) -> {
            this.dispose();
        });
    }
    @Override
    public void update() {
        this.clearInventory();
        for (MineBlock mineBlock : mine.getSpawningBlocks()) {
            int firstEmpty = this.getInventory().firstEmpty();
            if (firstEmpty == -1) return;
            List<String> blockInfoLore = new ArrayList<String>(LanguageContainer.translate("block-action-lore", List.class));
            ItemBuilder builder = ItemBuilder.from(mineBlock.getMaterial())
                    .withLore(
                            blockInfoLore.stream()
                                    .map(string -> string.replace("{CHANCE}", String.valueOf(mineBlock.getChance())))
                                    .toList()
                    );
            this.setItem(builder.build(), firstEmpty).onInventoryClick((event, player) -> {
                this.onBlockClick(event, firstEmpty, mineBlock, player);
            });
        }
    }

    private void onBlockClick(InventoryClickEvent event, int firstEmpty, MineBlock mineBlock, Player player) {
        if (event.getClickedInventory() == null || !(event.getClickedInventory().getHolder() instanceof MenuHolder)) return;
        if (event.isLeftClick()) {
            this.setItem((ItemStack) null, firstEmpty);
            mine.removeSpawningBlock(mineBlock);
        } else if (event.isRightClick()) {
            player.closeInventory();
            modificationService.addAction(player.getUniqueId(), new Notification(LanguageContainer.translate("modification-percentage-set", String.class), chatEvent -> {
                if (!NumberUtils.isDigits(chatEvent.getMessage())) {
                    player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("must-be-number", String.class)));
                    return;
                }
                double percent = Double.parseDouble(chatEvent.getMessage());
                if (percent > 100 || percent < 0) {
                    player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("percent-error", String.class)));
                    return;
                }
                mine.updateMineBlockChance(mineBlock, percent);
                modificationService.removeAction(player.getUniqueId());
                BlockChangeMenu blockChangeMenu = new BlockChangeMenu(mine);
                Bukkit.getScheduler().runTask(CocosMines.getInstance(), () -> player.openInventory(blockChangeMenu.getInventory()));
            }));
            new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
        }
    }
}
