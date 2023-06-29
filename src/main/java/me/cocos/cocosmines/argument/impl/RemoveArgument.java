package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.helper.ChatHelper;
import org.bukkit.entity.Player;

public final class RemoveArgument implements Argument {

    private final MineService mineService;

    public RemoveArgument(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("correct-usage", String.class) + "&e/cocosmine remove" + this.getArguments()));
            return;
        }
        Mine mine = mineService.findMineByName(args[1]);
        if (mine == null) {
            player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("cannot-find-mine", String.class) + args[1]));
            return;
        }
        player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("remove-mine-message", String.class) + args[1]));
        mineService.removeMine(mine);
    }

    @Override
    public String getDescription() {
        return LanguageContainer.translate("remove-description", String.class);
    }

    @Override
    public String getArguments() {
        return LanguageContainer.translate("remove-arguments", String.class);
    }

    @Override
    public String getName() {
        return "remove";
    }
}
