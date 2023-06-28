package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import me.cocos.cocosmines.data.Notification;
import me.cocos.cocosmines.helper.MaterialHelper;
import me.cocos.cocosmines.runnable.ModificationInfoRunnable;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.helper.ChatHelper;
import me.cocos.menu.holder.MenuHolder;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BlockChangeMenu extends Menu {

    private final Mine mine;
    private final ModificationService modificationService;

    public BlockChangeMenu(Mine mine) {
        super("&8>> &7Zmien bloki", 3);
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
            mine.getSpawningBlocks().add(mineBlock);
            int firstEmpty = this.getInventory().firstEmpty();
            ItemBuilder builder = ItemBuilder.from(mineBlock.getMaterial())
                    .withLore(
                            "",
                            "&8[&fLPM&8] &8- &7Usuwa blok z generatora",
                            "&8[&fPPM&8] &8- &7Ustawia % na wygenerowanie",
                            "",
                            "&8● &7Aktualny %&8: &6" + mineBlock.getChance() + "%"
                    );
            this.setItem(builder.build(), firstEmpty).onInventoryClick((e, p) -> {
                if (event.getClickedInventory() == null || !(event.getClickedInventory().getHolder() instanceof MenuHolder)) return;
                if (e.isLeftClick()) {
                    this.setItem((ItemStack) null, firstEmpty);
                    mine.getSpawningBlocks().remove(mineBlock);
                } else if (e.isRightClick()) {
                    modificationService.addAction(player.getUniqueId(), new Notification("&7Napisz &a% &7na chacie!", chatEvent -> {
                        if (chatEvent.getMessage().equalsIgnoreCase("Anuluj")) {
                            modificationService.removeAction(player.getUniqueId());
                            return;
                        }
                        if (!NumberUtils.isNumber(chatEvent.getMessage())) {
                            player.sendMessage(ChatHelper.coloredText("&cMusisz wpisac liczbe!"));
                            return;
                        }
                        double percent = Double.parseDouble(chatEvent.getMessage());
                        if (percent > 100 || percent < 0) {
                            player.sendMessage(ChatHelper.coloredText("&c% nie moze byc wiekszy niz 100 lub mniejszy niz 0"));
                            return;
                        }
                        mineBlock.setChance(percent);
                        modificationService.removeAction(player.getUniqueId());
                    }));
                    new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
                }
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
            ItemBuilder builder = ItemBuilder.from(mineBlock.getMaterial())
                    .withLore(
                            "",
                            "&8[&fLPM&8] &8- &7Usuwa blok z generatora",
                            "&8[&fPPM&8] &8- &7Ustawia % na wygenerowanie",
                            "",
                            "&8● &7Aktualny %&8: &6" + mineBlock.getChance() + "%"
                    );
            this.setItem(builder.build(), firstEmpty).onInventoryClick((event, player) -> {
                if (event.getClickedInventory() == null || !(event.getClickedInventory().getHolder() instanceof MenuHolder)) return;
                if (event.isLeftClick()) {
                    this.setItem((ItemStack) null, firstEmpty);
                    mine.getSpawningBlocks().remove(mineBlock);
                } else if (event.isRightClick()) {
                    modificationService.addAction(player.getUniqueId(), new Notification("&7Napisz &a% &7na chacie!", chatEvent -> {
                        if (chatEvent.getMessage().equalsIgnoreCase("Anuluj")) {
                            modificationService.removeAction(player.getUniqueId());
                            return;
                        }
                        if (!NumberUtils.isNumber(chatEvent.getMessage())) {
                            player.sendMessage(ChatHelper.coloredText("&cMusisz wpisac liczbe!"));
                            return;
                        }
                        double percent = Double.parseDouble(chatEvent.getMessage());
                        if (percent > 100 || percent < 0) {
                            player.sendMessage(ChatHelper.coloredText("&c% nie moze byc wiekszy niz 100 lub mniejszy niz 0"));
                            return;
                        }
                        mineBlock.setChance(percent);
                        modificationService.removeAction(player.getUniqueId());
                    }));
                    new ModificationInfoRunnable(modificationService, player).runTaskTimerAsynchronously(CocosMines.getInstance(), 0, 20);
                }
            });
        }
    }
}
