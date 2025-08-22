package me.cocos.cocosmines.placeholder;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.TimeHelper;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public final class PlaceholderReplacer extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "cocosmines";
    }

    @Override
    public @NotNull String getAuthor() {
        return "cocos";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getVersion() {
        return "2.7";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("time")) {
            String[] split = params.split("_");
            Mine mine = CocosMines.getInstance().getMineService().findMineByName(split[1]);
            if (mine != null) {
                return TimeHelper.convertMillisecondsToTime(mine.getLastRegenerationTime() - System.currentTimeMillis());
            }
        }
        return super.onRequest(player, params);
    }
}
