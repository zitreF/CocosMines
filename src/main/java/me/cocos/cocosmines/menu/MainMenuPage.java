package me.cocos.cocosmines.menu;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Pagination;
import me.cocos.cocosmines.helper.LocationHelper;
import me.cocos.cocosmines.helper.TimeHelper;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.menu.impl.MineEditMenu;
import me.cocos.gui.builder.gui.GuiBuilder;
import me.cocos.gui.builder.item.impl.ItemBuilder;
import me.cocos.gui.data.GuiItem;
import me.cocos.gui.gui.Gui;
import me.cocos.gui.pattern.Pattern;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public final class MainMenuPage {

    private final Gui gui;
    private final Pagination<Mine> pagination;
    private int page;

    public MainMenuPage(List<Mine> mines, int page) {
        this.pagination = new Pagination<>(28, mines);
        this.page = page;
        this.gui = GuiBuilder.normal("&8>> &e&lCocos&6&lMines", 6)
                .blockPlayerInventory(true)
                .disposable(true)
                .onClick((event, player) -> event.setCancelled(true))
                .build();
        this.update();
    }

    private void update() {
        this.gui.getInventory().clear();
        gui.applyPattern(Pattern.BORDER, GuiItem.of(Material.GRAY_STAINED_GLASS_PANE));
        GuiItem next = new ItemBuilder(Material.GREEN_BANNER)
                .name("&8>> &aNastepna strona &8<<")
                .flag(ItemFlag.HIDE_ENCHANTS)
                .enchantment(Enchantment.DURABILITY, 10)
                .asGuiItem()
                .onClick((event, player) -> {
                    if (!pagination.hasPage(page + 1)) return;
                    this.page++;
                    this.update();
                });
        GuiItem previous = new ItemBuilder(Material.RED_BANNER)
                .name("&8>> &cPoprzednia strona &8<<")
                .flag(ItemFlag.HIDE_ENCHANTS)
                .enchantment(Enchantment.DURABILITY, 10)
                .asGuiItem()
                .onClick((event, player) -> {
                    if (!pagination.hasPage(page - 1)) return;
                    this.page--;
                    this.update();
                });
        this.gui.setItem(45, previous);
        this.gui.setItem(53, next);
        for (Mine mine : pagination.getPage(page)) {
            int empty = this.gui.getInventory().firstEmpty();
            if (empty == -1) {
                return;
            }
            List<String> blockInfoLore = new ArrayList<String>(LanguageContainer.translate("block-info-lore", List.class));
            GuiItem item = ItemBuilder.from(mine.getLogo())
                    .name("&a&l" + mine.getName())
                    .lore(
                            blockInfoLore.stream()
                                    .map(string -> string
                                            .replace("{OWNER}", mine.getOwner())
                                            .replace("{CREATION-TIME}", TimeHelper.format(mine.getCreationTime()))
                                            .replace("{LOCATION}", LocationHelper.locationToString(mine.getFirstLocation())))
                                    .toList()
                    )
                    .asGuiItem()
                    .onClick((event, player) -> {
                        MineEditMenu mineEditMenu = new MineEditMenu(mine);
                        mineEditMenu.open(player);
                    });
            this.gui.setItem(empty, item);
        }
    }

    public void open(Player player) {
        this.gui.open(player);
    }
}
