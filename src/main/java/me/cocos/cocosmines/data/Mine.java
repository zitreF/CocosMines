package me.cocos.cocosmines.data;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.helper.ChanceHelper;
import me.cocos.cocosmines.helper.MaterialHelper;
import me.cocos.cocosmines.runnable.MineRegenerationRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class Mine {

    private static final FixedMetadataValue METADATA_VALUE = new FixedMetadataValue(CocosMines.getInstance(), "mine");
    private final MineRegenerationRunnable mineRegenerationRunnable;
    private String name;
    private final String owner;
    private final long creationTime;
    private long regenTime;
    private long lastRegenerationTime;
    private final World world;
    private Material logo;
    private final List<MineBlock> spawningBlocks;
    private final List<Block> blocks;
    private final List<List<Block>> splitList;
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
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        secondLocation.setWorld(world);
        if (spawningBlocks.isEmpty()) spawningBlocks.add(new MineBlock(50, Material.STONE));
        this.blocks = new ArrayList<>();
        this.mineRegenerationRunnable = new MineRegenerationRunnable(this);
        this.task = Bukkit.getScheduler().runTaskTimer(CocosMines.getInstance(), mineRegenerationRunnable, 20L, regenTime*20L);
        Location firstClone = firstLocation.clone();
        this.hologram = DHAPI.createHologram(UUID.randomUUID().toString(), firstClone.add(secondLocation).multiply(1/2d).add(0.5, 1d, 0.5), false, List.of("Tworze hologram..."));
        this.updateLocation(firstLocation, secondLocation);
        this.splitList = new ArrayList<>();
        int blockSize = blocks.size() / 4;
        int remainingBlocks = blocks.size() % 4;
        int currentIndex = 0;

        for (int i = 0; i < 4; i++) {
            int size = blockSize + (i < remainingBlocks ? 1 : 0);
            List<Block> sublist = blocks.subList(currentIndex, currentIndex + size);
            splitList.add(sublist);
            currentIndex += size;
        }
    }

    public void regenerate() {
        new BukkitRunnable() {
            private int currentIndex = 0;
            @Override
            public void run() {
                for (Block block : splitList.get(currentIndex)) {
                    Material randomMaterial = ChanceHelper.getRandomMaterial(spawningBlocks);
                    block.setType(randomMaterial);
                }
                currentIndex++;
                if (currentIndex == splitList.size()) {
                    this.cancel();
                }
            }
        }.runTaskTimer(CocosMines.getInstance(), 0, 1);
    }

    public long getLastRegenerationTime() {
        return lastRegenerationTime;
    }

    public void setLastRegenerationTime(long time) {
        this.lastRegenerationTime = time;
    }

    public void updateLocation(Location first, Location second) {
        if (!blocks.isEmpty()) {
            for (Block block : blocks) {
                block.removeMetadata("mine", CocosMines.getInstance());
            }
            blocks.clear();
        }

        int minX = Math.min(first.getBlockX(), second.getBlockX());
        int minY = Math.min(first.getBlockY(), second.getBlockY());
        int minZ = Math.min(first.getBlockZ(), second.getBlockZ());
        int maxX = Math.max(first.getBlockX(), second.getBlockX());
        int maxY = Math.max(first.getBlockY(), second.getBlockY());
        int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());

        this.firstLocation.setX(minX);
        this.firstLocation.setY(minY);
        this.firstLocation.setZ(minZ);
        this.secondLocation.setX(maxX);
        this.secondLocation.setY(maxY);
        this.secondLocation.setZ(maxZ);
        this.hologram.setLocation(firstLocation.clone().add(secondLocation).multiply(0.5d));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setMetadata("mine", METADATA_VALUE);
                    this.blocks.add(block);
                }
            }
        }
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
