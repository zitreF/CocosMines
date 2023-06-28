package me.cocos.cocosmines.helper;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import org.bukkit.Material;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ChanceHelper {

    private ChanceHelper() {}

    public static Material getRandomMaterial(List<MineBlock> blocks) {
        double totalChance = 0.0;
        for (MineBlock block : blocks) {
            totalChance += block.getChance();
        }

        double randNum = Math.random() * totalChance;
        double cumulativeChance = 0.0;

        for (MineBlock block : blocks) {
            cumulativeChance += block.getChance();
            if (randNum <= cumulativeChance) {
                return block.getMaterial();
            }
        }

        return blocks.get(ThreadLocalRandom.current().nextInt(blocks.size())).getMaterial();
    }
}
