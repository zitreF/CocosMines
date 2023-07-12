package me.cocos.cocosmines.helper;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ChanceHelper {

    private ChanceHelper() {}

    public static Material getRandomMaterial(List<MineBlock> blocks) {
        if (blocks.isEmpty()) {
            return Material.STONE;
        }
        return blocks.stream()
                .filter(block -> ThreadLocalRandom.current().nextDouble(100) < block.getChance())
                .map(MineBlock::getMaterial)
                .findFirst()
                .orElseGet(() -> ChanceHelper.getRandomMaterial(blocks));
    }

    public static ItemStack getRandomItemStack(List<MineBlock> blocks) {
        if (blocks.isEmpty()) {
            return new ItemStack(Material.STONE);
        }
        return blocks.stream()
                .filter(block -> ThreadLocalRandom.current().nextDouble(100) < block.getChance())
                .map(mineBlock -> new ItemStack(mineBlock.getMaterial()))
                .findFirst()
                .orElseGet(() -> ChanceHelper.getRandomItemStack(blocks));
    }
}
