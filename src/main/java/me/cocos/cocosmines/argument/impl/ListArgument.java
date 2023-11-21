package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;
import me.cocos.gui.helper.ChatHelper;
import org.bukkit.entity.Player;

public final class ListArgument implements Argument {

    private final MineService mineService;

    public ListArgument(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("");
        player.sendMessage(ChatHelper.colored(LanguageContainer.translate("list-message", String.class)));
        for (Mine mine : mineService.getMines()) {
            player.sendMessage(ChatHelper.colored("&8- &f" + mine.getName() + " &8- &7" + mine.getOwner()));
        }
        player.sendMessage("");
    }

    @Override
    public String getDescription() {
        return LanguageContainer.translate("list-description", String.class);
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public String getName() {
        return "list";
    }
}
