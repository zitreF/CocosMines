package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;
import me.cocos.gui.helper.ChatHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class TeleportArgument implements Argument {

    private final MineService mineService;

    public TeleportArgument(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("correct-usage", String.class) + "&e/cocosmine tp" + this.getArguments()));
            return;
        }
        Mine mine = mineService.findMineByName(args[1]);
        if (mine == null) {
            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("cannot-find-mine", String.class) + args[1]));
            return;
        }
        Location first = mine.getFirstLocation().clone();
        Location second = mine.getSecondLocation().clone();
        int bonusY = second.getBlockY()-first.getBlockY();
        player.teleport(first.add(second).multiply(1/2d).add(0, bonusY/2d, 0));
    }

    @Override
    public String getDescription() {
        return LanguageContainer.translate("teleport-description", String.class);
    }

    @Override
    public String getArguments() {
        return LanguageContainer.translate("teleport-arguments", String.class);
    }

    @Override
    public String getName() {
        return "tp";
    }
}
