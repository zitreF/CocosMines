package me.cocos.cocosmines.command;

import me.cocos.cocosmines.argument.Argument;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.service.ArgumentService;
import me.cocos.menu.helper.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MineCommand implements CommandExecutor {

    private final ArgumentService argumentService;

    public MineCommand(ArgumentService argumentService) {
        this.argumentService = argumentService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("cocosmines.admin")) {
            player.sendMessage(ChatHelper.coloredText(LanguageContainer.translate("no-permission", String.class)));
            return false;
        }
        if (args.length == 0) {
            this.sendCorrectUsageMessage(player);
            return false;
        }
        Argument argument = argumentService.getArgumentByName(args[0]);
        if (argument == null) {
            this.sendCorrectUsageMessage(player);
            return false;
        }
        argument.execute(player, args);
        return false;
    }

    private void sendCorrectUsageMessage(Player player) {
        player.sendMessage(ChatHelper.coloredText("&8&m|>>----)&e&l Cocos&6&lMines &8&m(----<<"));
        player.sendMessage(ChatHelper.coloredText("&8&m|"));
        for (Argument argument : argumentService.getArguments()) {
            player.sendMessage(ChatHelper.coloredText("&8&m|&e /cocosmine " + argument.getName() + argument.getArguments() + " &8- &7" + argument.getDescription()));
        }
        player.sendMessage(ChatHelper.coloredText("&8&m|"));
        player.sendMessage(ChatHelper.coloredText("&8&m|>>----)&e&l Cocos&6&lMines &8&m(----<<"));
    }
}
