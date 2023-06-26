package me.cocos.cocosmines.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class Mine {

    private String name;
    private final String owner;
    private final long creationTime;
    private final World world;
    private final List<Material> spawningBlocks;
    private final Location firstLocation;
    private final Location secondLocation;

    public Mine(String name, String owner, long creationTime, Location firstLocation, Location secondLocation) {
        this.world = firstLocation.getWorld();
        this.name = name;
        this.owner = owner;
        this.creationTime = creationTime;
        this.spawningBlocks = new ArrayList<>();
        spawningBlocks.add(Material.DIRT);
        int startX = Math.min(firstLocation.getBlockX(), secondLocation.getBlockX());
        int startY = Math.min(firstLocation.getBlockY(), secondLocation.getBlockY());
        int startZ = Math.min(firstLocation.getBlockZ(), secondLocation.getBlockZ());
        int endX = Math.max(firstLocation.getBlockX(), secondLocation.getBlockX());
        int endY = Math.max(firstLocation.getBlockY(), secondLocation.getBlockY());
        int endZ = Math.max(firstLocation.getBlockZ(), secondLocation.getBlockZ());
        this.firstLocation = new Location(world, startX, startY, startZ);
        this.secondLocation = new Location(world, endX, endY, endZ);
    }

    public void regenerate() {
        for (int y = firstLocation.getBlockY(); y <= secondLocation.getBlockY(); y++) {
            for (int x = firstLocation.getBlockX(); x <= secondLocation.getBlockX(); x++) {
                for (int z = firstLocation.getBlockZ(); z <= secondLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(spawningBlocks.get(ThreadLocalRandom.current().nextInt(spawningBlocks.size())));
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public String getOwner() {
        return owner;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public List<Material> getSpawningBlocks() {
        return spawningBlocks;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }
}