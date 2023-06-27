package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.data.Mine;
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
            player.sendMessage(ChatHelper.coloredText("&8>> &7Poprawne uzycie&8: &e/cocosmine remove <nazwa>"));
            return;
        }
        Mine mine = mineService.findMineByName(args[1]);
        if (mine == null) {
            player.sendMessage(ChatHelper.coloredText("&cNie znaleziono kopalni o nazwie &l" + args[1]));
            return;
        }
        player.sendMessage(ChatHelper.coloredText("&aPomyslnie usunieto kopalnie &l" + args[1]));
        mineService.removeMine(mine);
    }

    @Override
    public String getDescription() {
        return "usuwa kopalnie";
    }

    @Override
    public String getArguments() {
        return " <nazwa>";
    }

    @Override
    public String getName() {
        return "remove";
    }
}
