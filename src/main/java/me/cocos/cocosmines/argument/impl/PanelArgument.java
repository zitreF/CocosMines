package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.menu.MainMenuPage;
import me.cocos.cocosmines.service.MineService;
import me.cocos.gui.helper.ChatHelper;
import org.bukkit.entity.Player;

public final class PanelArgument implements Argument {

    private final MineService mineService;

    public PanelArgument(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (mineService.getMines().isEmpty()) {
            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("panel-no-mines", String.class)));
            return;
        }
        MainMenuPage mainMenuPage = new MainMenuPage(mineService.getMines(), 0);
        mainMenuPage.open(player);
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
