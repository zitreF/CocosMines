package me.cocos.cocosmines.service;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.configuration.MineConfiguration;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import me.cocos.cocosmines.helper.LocationHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class MineService {

    private final List<Mine> mines;
    private final MineConfiguration configuration;

    public MineService(MineConfiguration configuration) {
        this.configuration = configuration;
        ConfigurationSection section = configuration.getConfig().getConfigurationSection("");
        this.mines = section
                .getKeys(false)
                .stream()
                .map(section::getConfigurationSection)
                .map(this::loadMine)
                .collect(Collectors.toList());
    }

    private Mine loadMine(ConfigurationSection section) {
        String owner = section.getString("owner");
        long creationTime = section.getLong("creationTime");
        long regenTime = section.getLong("regenTime");
        Material logo = Material.valueOf(section.getString("logo"));
        List<MineBlock> mineBlocks = Arrays.stream(section.getString("blocks").split(":")).map(string -> {
            String[] split = string.split("=");
            return new MineBlock(Double.parseDouble(split[1]), Material.valueOf(split[0]));
        }).collect(Collectors.toList());

        Location firstLocation = LocationHelper.locationFromString(section.getString("firstLocation"));
        Location secondLocation = LocationHelper.locationFromString(section.getString("secondLocation"));
        return new Mine(section.getName(), owner, creationTime, regenTime, logo, mineBlocks, firstLocation, secondLocation);
    }

    public void saveMines() {
        CompletableFuture.runAsync(() -> {
            for (Mine mine : mines) {
                ConfigurationSection cs = configuration.getConfig().getConfigurationSection(mine.getName());
                cs.set("owner", mine.getOwner());
                cs.set("creationTime", mine.getCreationTime());
                cs.set("regenTime", mine.getRegenTime());
                cs.set("logo", mine.getLogo().toString());
                cs.set("blocks", mine.getSpawningBlocks().stream().map(mineBlock -> mineBlock.getMaterial() + "=" + mineBlock.getChance()).collect(Collectors.joining(":")));
                cs.set("firstLocation", LocationHelper.locationToString(mine.getFirstLocation()));
                cs.set("secondLocation", LocationHelper.locationToString(mine.getSecondLocation()));
            }
            CompletableFuture.runAsync(() -> {
                try {
                    configuration.saveFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public Mine findMineByName(String name) {
        return this.mines.stream().filter(mine -> mine.getName().equals(name)).findFirst().orElse(null);
    }

    public void addMine(Mine mine) {
        this.mines.add(mine);
    }

    public void createMine(Mine mine) {
        ConfigurationSection section = configuration.getConfig().createSection(mine.getName());
        section.set("owner", mine.getOwner());
        section.set("creationTime", mine.getCreationTime());
        section.set("regenTime", mine.getRegenTime());
        section.set("logo", mine.getLogo().toString());
        section.set("blocks", mine.getSpawningBlocks().stream().map(mineBlock -> mineBlock.getMaterial() + "=" + mineBlock.getChance()).collect(Collectors.joining(":")));
        section.set("firstLocation", LocationHelper.locationToString(mine.getFirstLocation()));
        section.set("secondLocation", LocationHelper.locationToString(mine.getSecondLocation()));
        CompletableFuture.runAsync(() -> {
            try {
                configuration.saveFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeMine(Mine mine) {
        this.mines.remove(mine);
        mine.getHologram().delete();
        CompletableFuture.runAsync(() -> {
            configuration.getConfig().set(mine.getName(), null);
            try {
                configuration.saveFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Mine> getMines() {
        return Collections.unmodifiableList(this.mines);
    }
}
