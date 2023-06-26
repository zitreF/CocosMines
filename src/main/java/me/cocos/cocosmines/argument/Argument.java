package me.cocos.cocosmines.argument;

import org.bukkit.entity.Player;

public interface Argument {

    void execute(Player player, String[] args);

    String getDescription();

    String getArguments();

    String getName();
}
