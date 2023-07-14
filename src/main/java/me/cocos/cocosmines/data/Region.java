package me.cocos.cocosmines.data;

import org.bukkit.Location;
import org.bukkit.World;

public final class Region {
    private final Mine mine;

    public Region(Mine mine) {
        this.mine = mine;
    }

    public Mine getMine() {
        return mine;
    }

    public boolean isLocationWithinRegion(Location location) {
        Location firstLocation = mine.getFirstLocation();
        Location secondLocation = mine.getSecondLocation();
        World world = firstLocation.getWorld();
        if (world == null || !world.equals(location.getWorld())) {
            return false;
        }

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return (x >= firstLocation.getBlockX() && x <= secondLocation.getBlockX())
                && (y >= firstLocation.getBlockY() && y <= secondLocation.getBlockY())
                && (z >= firstLocation.getBlockZ() && z <= secondLocation.getBlockZ());
    }
}
