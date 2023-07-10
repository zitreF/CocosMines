package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.menu.MainMenuPage;
import org.bukkit.entity.Player;

import java.util.List;

public final class PanelArgument implements Argument {

    private final List<Mine> mines;

    public PanelArgument(List<Mine> mines) {
        this.mines = mines;
    }

    @Override
    public void execute(Player player, String[] args) {
        MainMenuPage mainMenuPage = new MainMenuPage(mines, 0);
        player.openInventory(mainMenuPage.getInventory());
    }

    @Override
    public String getDescription() {
        return LanguageContainer.translate("panel-description", String.class);
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
