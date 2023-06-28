package me.cocos.cocosmines.argument.impl;

import me.cocos.cocosmines.argument.Argument;
import org.bukkit.entity.Player;

public final class TeleportArgument implements Argument {

    @Override
    public void execute(Player player, String[] args) {

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
