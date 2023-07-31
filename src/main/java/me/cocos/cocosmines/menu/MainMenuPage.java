package me.cocos.cocosmines.menu;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Pagination;
import me.cocos.cocosmines.helper.LocationHelper;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.menu.impl.MineEditMenu;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.Menu;
import me.cocos.menu.builder.impl.ItemBuilder;
import me.cocos.menu.helper.GuiHelper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MainMenuPage extends Menu {

    private final Pagination<Mine> pagination;
    private int page;

    public MainMenuPage(List<Mine> mines, int page) {
        super("&8>> &e&lCocos&6&lMines", 6, true);
        this.pagination = new Pagination<>(28, mines);
        this.page = page;
        this.setOnInventoryClick((event, player) -> {
            event.setCancelled(true);
        });
        this.update();
    }

    private void update() {
        this.getInventory().clear();
        GuiHelper.border(this.getInventory(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        ItemBuilder next = new ItemBuilder(Material.GREEN_BANNER)
                .withItemName("&8>> &aNastepna strona &8<<")
                .flag(ItemFlag.HIDE_ENCHANTS)
                .withEnchantment(Enchantment.DURABILITY, 10);
        ItemBuilder previous = new ItemBuilder(Material.RED_BANNER)
                .withItemName("&8>> &cPoprzednia strona &8<<")
                .flag(ItemFlag.HIDE_ENCHANTS)
                .withEnchantment(Enchantment.DURABILITY, 10);
        this.setItem(previous.build(), 45).onInventoryClick((event, player) -> {
            if (!pagination.hasPage(page - 1)) return;
            this.page--;
            this.clearInventory();
            this.update();
        });
        this.setItem(next.build(), 53).onInventoryClick((event, player) -> {
            if (!pagination.hasPage(page + 1)) return;
            this.page++;
            this.clearInventory();
            this.update();
        });
        for (Mine mine : pagination.getPage(page)) {
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
