package me.cocos.cocosmines.helper;

import org.bukkit.Material;

public final class MaterialHelper {

    private MaterialHelper() {}

    public static boolean isValid(Material material) {
        return !material.hasGravity() && material.isBlock() && material.isSolid();
    }
}
