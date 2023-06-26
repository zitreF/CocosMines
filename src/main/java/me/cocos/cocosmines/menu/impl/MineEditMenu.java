package me.cocos.cocosmines.menu.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.service.ModificationService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.helper.GuiHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MineEditMenu extends Menu {

    private final Mine mine;

    public MineEditMenu(Mine mine) {
        super("&8>> &7Edytowanie &e&l" + mine.getName(), 3);
        this.mine = mine;
        ModificationService modificationService = CocosMines.getInstance().getModificationService();
        this.setOnInventoryClick(((event, player) -> event.setCancelled(true)));
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

        });
        this.setItem(items, 12).onInventoryClick((event, player) -> {

        });
        this.setItem(coords, 13).onInventoryClick((event, player) -> {

        });
        this.setItem(reset, 14).onInventoryClick((event, player) -> {

        });
        this.setItem(turnoff, 15).onInventoryClick((event, player) -> {

        });
        this.setItem(teleport, 15).onInventoryClick((event, player) -> {

        });

    }

    @Override
    public void update() {

    }
}
