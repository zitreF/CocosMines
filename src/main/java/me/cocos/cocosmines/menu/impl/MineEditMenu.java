package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.builder.impl.MenuBuilder;
import me.cocos.menu.helper.GuiHelper;
import me.cocos.menu.type.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class MineEditMenu extends Menu {

    private final Mine mine;

    public MineEditMenu(Mine mine) {
        super("&8>> &7Edytowanie &e&l" + mine.getName(), 3);
        this.mine = mine;
        ModificationService modificationService = CocosMines.getInstance().getModificationService();
        this.setOnInventoryClick(((event, player) -> event.setCancelled(true)));
        this.setOnInventoryClose((event, player) -> this.dispose());
        GuiHelper.border(this.getInventory(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        ItemStack name = ItemBuilder.from(Material.NAME_TAG).withItemName("&8● &7Zmien nazwe").build();
        ItemStack icon = ItemBuilder.from(Material.EMERALD_ORE).withItemName("&8● &7Ustaw ikone").build();
        ItemStack items = ItemBuilder.from(Material.GRASS_BLOCK).withItemName("&8● &7Zmien itemy").build();
        ItemStack coords = ItemBuilder.from(Material.COMPASS).withItemName("&8● &7Zmien lokalizacje").build();
        ItemStack reset = ItemBuilder.from(Material.ANVIL).withItemName("&8● &7Resetuj kopalnie").build();
        ItemStack turnoff = ItemBuilder.from(Material.BARRIER).withItemName("&8● &7Wylacz kopalnie").build();
        ItemStack teleport = ItemBuilder.from(Material.ENDER_PEARL).withItemName("&8● &7Przeteleportuj").build();
        this.setItem(name, 10).onInventoryClick((event, player) -> {
            player.closeInventory();
            modificationService.addAction(player.getUniqueId(), chatEvent -> {
                String message = chatEvent.getMessage();
                mine.setName(message);
                modificationService.removeAction(player.getUniqueId());
            });
        });
        this.setItem(icon, 11).onInventoryClick((event, player) -> {
            player.closeInventory();
            Menu menu = MenuBuilder.from(MenuType.SIMPLE, "&8>> &7Zmien ikone", 1)
                    .blockPlayerInventory(false)
                    .build();
            menu.setOnInventoryClose((event2, player2) -> {
                mine.setLogo(menu.getInventory().getItem(0).getType());
                menu.dispose();
            });
            player.openInventory(menu.getInventory());
        });
        this.setItem(items, 12).onInventoryClick((event, player) -> {
            player.closeInventory();
            Menu menu = MenuBuilder.from(MenuType.SIMPLE, "&8>> &7Zmien bloki", 1)
                    .blockPlayerInventory(false)
                    .addItems(mine.getSpawningBlocks().stream().map(ItemStack::new).toArray(ItemStack[]::new))
                    .build();
            menu.setOnInventoryClose((event2, player2) -> {
                mine.getSpawningBlocks().clear();
                for (ItemStack item : menu.getInventory().getContents()) {
                    if (item == null) continue;
                    mine.getSpawningBlocks().add(item.getType());
                }
                menu.dispose();
            });
            player.openInventory(menu.getInventory());
        });
        this.setItem(coords, 13).onInventoryClick((event, player) -> {

        });
        this.setItem(reset, 14).onInventoryClick((event, player) -> {
            mine.regenerate();
            // restart timer
        });
        this.setItem(turnoff, 15).onInventoryClick((event, player) -> {

        });
        this.setItem(teleport, 15).onInventoryClick((event, player) -> {
            Location first = mine.getFirstLocation().clone();
            Location second = mine.getSecondLocation().clone();
            int bonusY = second.getBlockY()-first.getBlockY();
            player.teleport(first.add(second).multiply(1/2d).add(0, bonusY/2d, 0));
        });

    }

    @Override
    public void update() {

    }
}
