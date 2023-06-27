package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.service.MineService;
import me.cocos.menu.helper.ChatHelper;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class CreateArgument implements Argument {

    private final MineService mineService;

    public CreateArgument(MineService mineService) {
        this.mineService = mineService;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(ChatHelper.coloredText("&8>> &7Poprawne uzycie&8: &e/cocosmine create <nazwa> <czas-regeneracji>"));
            return;
        }
        if (!NumberUtils.isNumber(args[2])) {
            player.sendMessage(ChatHelper.coloredText("&8>> &7Poprawne uzycie&8: &e/cocosmine create <nazwa> <czas-regeneracji>"));
            return;
        }
        long regenTime = Long.parseLong(args[2]);
        Mine mine = new Mine(args[1], player.getName(), System.currentTimeMillis(), regenTime, Material.BEDROCK, player.getLocation(), player.getLocation().add(10, 10, 10));
        mine.regenerate();
        mineService.createMine(mine);
        mineService.addMine(mine);
    }

    @Override
    public String getDescription() {
        return "tworzy kopalnie";
    }

    @Override
    public String getArguments() {
        return " <nazwa>";
    }

    @Override
    public String getName() {
        return "create";
    }
}
