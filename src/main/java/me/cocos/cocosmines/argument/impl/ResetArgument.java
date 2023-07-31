package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.helper.ChatHelper;
import org.apache.commons.lang.CharUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public final class ResetArgument implements Argument {

    private final MineService mineService;

    public ResetArgument(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("correct-usage", String.class) + "&e/cocosmine reset" + this.getArguments()));
            return;
        }
        Mine mine = mineService.findMineByName(args[1]);
        if (mine == null) {
            player.sendMessage(ChatHelper.colored(LanguageContainer.translate("cannot-find-mine", String.class) + args[1]));
            return;
        }
        mine.regenerate();
        mine.setLastRegenerationTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(mine.getRegenTime()));
        player.sendMessage(ChatHelper.colored(LanguageContainer.translate("reset-success", String.class)));
    }

    @Override
    public String getDescription() {
        return LanguageContainer.translate("reset-description", String.class);
    }

    @Override
    public String getArguments() {
        return LanguageContainer.translate("reset-arguments", String.class);
    }

    @Override
    public String getName() {
        return "reset";
    }
}
