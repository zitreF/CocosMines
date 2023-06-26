package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.menu.MainMenu;
import org.bukkit.entity.Player;

public final class PanelArgument implements Argument {

    private final MainMenu menu;

    public PanelArgument(MainMenu menu) {
        this.menu = menu;
    }

    @Override
    public void execute(Player player, String[] args) {
        menu.update();
        player.openInventory(menu.getInventory());
    }

    @Override
    public String getDescription() {
        return "otwiera panel z kopalniami";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public String getName() {
        return "panel";
    }
}
