package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.helper.ChatHelper;
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
            player.sendMessage(ChatHelper.coloredText("&8>> &7Poprawne uzycie&8: &e/cocosmine tp <nazwa>"));
            return;
        }
        Mine mine = mineService.findMineByName(args[1]);
        if (mine == null) {
            player.sendMessage(ChatHelper.coloredText("&cNie znaleziono kopalni o nazwie &l" + args[1]));
            return;
        }
        Location first = mine.getFirstLocation().clone();
        Location second = mine.getSecondLocation().clone();
        int bonusY = second.getBlockY()-first.getBlockY();
        player.teleport(first.add(second).multiply(1/2d).add(0, bonusY/2d, 0));
    }

    @Override
    public String getDescription() {
        return "teleportuje do kopalni";
    }

    @Override
    public String getArguments() {
        return " <nazwa>";
    }

    @Override
    public String getName() {
        return "tp";
    }
}
