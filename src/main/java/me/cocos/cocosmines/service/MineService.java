package me.cocos.cocosmines.service;

import me.cocos.cocosmines.configuration.MineConfiguration;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.MineBlock;
import me.cocos.cocosmines.data.Region;
import me.cocos.cocosmines.helper.LocationHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class MineService {

    private final RegionService regionService;
    private final MineConfiguration configuration;

    public MineService(MineConfiguration configuration) {
        this.configuration = configuration;
        this.regionService = new RegionService();

        ConfigurationSection section = configuration.getConfig().getConfigurationSection("");
        for (String key : section.getKeys(false)) {
            ConfigurationSection mineSection = section.getConfigurationSection(key);
            Mine mine = this.loadMine(mineSection);
            regionService.addMineToRegion(mine);
        }
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
            for (Mine mine : regionService.getAllMines()) {
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
        return regionService.findMineByName(name);
    }

    public void addMine(Mine mine) {
        regionService.addMineToRegion(mine);
    }

    public Mine findMineByBlock(Location blockLocation) {
        Region region = regionService.getRegionByLocation(blockLocation);
        if (region != null) {
            return region.getMine();
        }
        return null;
    }

    public void createMine(Mine mine) {
        regionService.addMineToRegion(mine);
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
        regionService.removeMineFromRegion(mine);
        if (mine.getHologram() != null) {
            mine.getHologram().delete();
        }
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
        return Collections.unmodifiableList(regionService.getAllMines());
    }
}
