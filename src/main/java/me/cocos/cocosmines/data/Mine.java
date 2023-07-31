package me.cocos.cocosmines.data;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.cocos.cocosmines.CocosMines;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public final class Mine {

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
    private RandomPattern randomPattern;

    private final Hologram hologram;

    private Region region;

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
        Location firstClone = firstLocation.clone();
        this.hologram = (CocosMines.getInstance().getHookService().useHologram()
                ? DHAPI.createHologram(UUID.randomUUID().toString(), firstClone.add(secondLocation).multiply(1/2d).add(0.5, 1d, 0.5), false, List.of("Tworze hologram..."))
                : null);
        this.updateLocation(firstLocation, secondLocation);
        this.updateRandomPattern();
    }

    private void updateRandomPattern() {
        this.randomPattern = new RandomPattern();
        for (MineBlock mineBlock : this.spawningBlocks) {
            randomPattern.add(BukkitAdapter.adapt(mineBlock.getMaterial().createBlockData()), mineBlock.getChance());
        }
    }

    public void regenerate() {
        EditSession editSession = CocosMines.getInstance().getEditSession(region.getWorld());
        editSession.setBlocks(region, this.randomPattern);
    }

    public long getLastRegenerationTime() {
        return lastRegenerationTime;
    }

    public void setLastRegenerationTime(long time) {
        this.lastRegenerationTime = time;
    }

    public void updateLocation(Location first, Location second) {
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

        if (CocosMines.getInstance().getHookService().useHologram()) this.hologram.setLocation(firstLocation.clone().add(secondLocation).multiply(0.5d));

        this.region = new CuboidRegion(BukkitAdapter.adapt(firstLocation.getWorld()), BlockVector3.at(firstLocation.getX(), firstLocation.getY(),
                firstLocation.getZ()), BlockVector3.at(secondLocation.getX(), secondLocation.getY(), secondLocation.getZ()));
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
    }

    public Material getLogo() {
        return logo;
    }

    public void setLogo(Material logo) {
        this.logo = logo;
    }

    public void addSpawningBlock(MineBlock mineBlock) {
        this.spawningBlocks.add(mineBlock);
        this.updateRandomPattern();
    }

    public void removeSpawningBlock(MineBlock mineBlock) {
        this.spawningBlocks.remove(mineBlock);
        this.updateRandomPattern();
    }

    public void updateMineBlockChance(MineBlock block, double percent) {
        block.setChance(percent);
        this.updateRandomPattern();
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

}
