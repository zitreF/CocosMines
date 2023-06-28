package me.cocos.cocosmines.data;

import org.bukkit.Material;

public final class MineBlock {

    private double chance;
    private final Material material;

    public MineBlock(double chance, Material material) {
        this.chance = chance;
        this.material = material;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public Material getMaterial() {
        return material;
    }
}
