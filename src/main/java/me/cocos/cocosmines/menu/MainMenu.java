package me.cocos.cocosmines.menu;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.LocationHelper;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.menu.impl.MineEditMenu;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.Menu;
import me.cocos.menu.animation.Animation;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.helper.GuiHelper;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public final class MainMenu extends Menu {

    private final MineService mineService;

    public MainMenu(MineService mineService) {
        super("&8>> &e&lCocos&6&lMines", 6);
        this.mineService = mineService;
        this.setOnInventoryClick((event, player) -> {
            event.setCancelled(true);
        });
    }

    // 5 minut
//    @Scheduled(delay = 6000L, repeat = 6000L, async = true)
    @Override
    public void update() {
        this.getInventory().clear();
        GuiHelper.border(this.getInventory(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        for (Mine mine : mineService.getMines()) {
            int empty = this.getInventory().firstEmpty();
            if (empty == -1) {
                // create new page :)
                return;
            }
            ItemBuilder item = ItemBuilder.from(mine.getLogo())
                    .withItemName("&a&l" + mine.getName())
                    .withLore(
                            "",
                            "&8● &7Tworca: &e" + mine.getOwner(),
                            "&8● &7Data zalozenia: &e" + TimeHelper.format(mine.getCreationTime()),
                            "&8● &7Kordy: &e" + LocationHelper.locationToString(mine.getFirstLocation()),
                            ""
                    );
            this.setItem(item.build(), empty).onInventoryClick((event, player) -> {
                MineEditMenu mineEditMenu = new MineEditMenu(mine);
                player.openInventory(mineEditMenu.getInventory());
            });
        }
    }
}
