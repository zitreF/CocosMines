package me.cocos.cocosmines.menu;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.LocationHelper;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.menu.impl.MineEditMenu;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.helper.GuiHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MainMenu extends Menu {

    private final MineService mineService;

    public MainMenu(MineService mineService) {
        super("&8>> &e&lCocos&6&lMines", 6);
        this.mineService = mineService;
        this.setOnInventoryClick((event, player) -> {
            event.setCancelled(true);
        });
    }

    @Override
    public void update() {
        this.getInventory().clear();
        GuiHelper.border(this.getInventory(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        for (Mine mine : mineService.getMines()) {
            int empty = this.getInventory().firstEmpty();
            if (empty == -1) {
                return;
            }
            List<String> blockInfoLore = new ArrayList<String>(LanguageContainer.translate("block-info-lore", List.class));
            ItemBuilder item = ItemBuilder.from(mine.getLogo())
                    .withItemName("&a&l" + mine.getName())
                    .withLore(
                            blockInfoLore.stream()
                                    .map(string -> string
                                    .replace("{OWNER}", mine.getOwner())
                                    .replace("{CREATION-TIME}", TimeHelper.format(mine.getCreationTime()))
                                    .replace("{LOCATION}", LocationHelper.locationToString(mine.getFirstLocation())))
                                    .toList()
                    );
            this.setItem(item.build(), empty).onInventoryClick((event, player) -> {
                MineEditMenu mineEditMenu = new MineEditMenu(mine);
                player.openInventory(mineEditMenu.getInventory());
            });
        }
    }
}
