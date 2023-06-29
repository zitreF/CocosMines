package me.cocos.cocosmines.data;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.helper.ChanceHelper;
import me.cocos.cocosmines.runnable.MineRegenerationRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class Mine {

    private final MineRegenerationRunnable mineRegenerationRunnable;
    private String name;
    private final String owner;
    private final long creationTime;
    private long regenTime;
    private long lastRegenerationTime;
    private final World world;
    private Material logo;
    private final List<MineBlock> spawningBlocks;
    private final Location firstLocation;
    private final Location secondLocation;

    private final Hologram hologram;
    private BukkitTask task;

    public Mine(String name, String owner, long creationTime, long regenTime, Material logo, List<MineBlock> spawningBlocks, Location firstLocation, Location secondLocation) {
        this.world = firstLocation.getWorld();
        this.name = name;
        this.owner = owner;
        this.creationTime = creationTime;
        this.regenTime = regenTime;
        this.logo = logo;
        this.spawningBlocks = spawningBlocks;
        if (spawningBlocks.isEmpty()) spawningBlocks.add(new MineBlock(50, Material.STONE));
        int startX = Math.min(firstLocation.getBlockX(), secondLocation.getBlockX());
        int startY = Math.min(firstLocation.getBlockY(), secondLocation.getBlockY());
        int startZ = Math.min(firstLocation.getBlockZ(), secondLocation.getBlockZ());
        int endX = Math.max(firstLocation.getBlockX(), secondLocation.getBlockX());
        int endY = Math.max(firstLocation.getBlockY(), secondLocation.getBlockY());
        int endZ = Math.max(firstLocation.getBlockZ(), secondLocation.getBlockZ());
        this.firstLocation = new Location(world, startX, startY, startZ);
        this.secondLocation = new Location(world, endX, endY, endZ);
        this.mineRegenerationRunnable = new MineRegenerationRunnable(this);
        this.task = Bukkit.getScheduler().runTaskTimer(CocosMines.getInstance(), mineRegenerationRunnable, 20L, regenTime*20L);
        Location firstClone = firstLocation.clone();
        this.hologram = DHAPI.createHologram(UUID.randomUUID().toString(), firstClone.add(secondLocation).multiply(1/2d).add(0.5, 1d, 0.5), false, List.of("Tworze hologram..."));
    }

    public void regenerate() {
        for (int y = firstLocation.getBlockY(); y <= secondLocation.getBlockY(); y++) {
            for (int x = firstLocation.getBlockX(); x <= secondLocation.getBlockX(); x++) {
                for (int z = firstLocation.getBlockZ(); z <= secondLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Material material = ChanceHelper.getRandomMaterial(this.spawningBlocks);
                    block.setType(material);
                }
            }
        }
    }

    public long getLastRegenerationTime() {
        return lastRegenerationTime;
    }

    public void setLastRegenerationTime(long time) {
        this.lastRegenerationTime = time;
    }

    public void updateLocation(Location first, Location second) {
        this.firstLocation.setX(Math.min(first.getBlockX(), second.getBlockX()));
        this.firstLocation.setY(Math.min(first.getBlockY(), second.getBlockY()));
        this.firstLocation.setZ(Math.min(first.getBlockZ(), second.getBlockZ()));
        this.secondLocation.setX(Math.max(first.getBlockX(), second.getBlockX()));
        this.secondLocation.setY(Math.max(first.getBlockY(), second.getBlockY()));
        this.secondLocation.setZ(Math.max(first.getBlockZ(), second.getBlockZ()));
        this.hologram.setLocation(firstLocation.clone().add(secondLocation).multiply(1/2d));
    }

    public void updateHologram(List<String> lines) {
        DHAPI.setHologramLines(hologram, lines);
    }

    public Hologram getHologram() {
        return hologram;
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

    public long getRegenTime() {
        return regenTime;
    }

    public void setRegenTime(long regenTime) {
        this.regenTime = regenTime;
        this.task.cancel();
        this.task = Bukkit.getScheduler().runTaskTimer(CocosMines.getInstance(), mineRegenerationRunnable, 20L, regenTime*20L);
    }

    public Material getLogo() {
        return logo;
    }

    public void setLogo(Material logo) {
        this.logo = logo;
    }

    public List<MineBlock> getSpawningBlocks() {
        return spawningBlocks;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public BukkitTask getTask() {
        return task;
    }

}
