package me.cocos.cocosmines.service;

import me.cocos.cocosmines.configuration.MineConfiguration;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.helper.LocationHelper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
                .peek(System.out::println)
                .map(section::getConfigurationSection)
                .map(this::loadMine)
                .collect(Collectors.toList());
    }

    private Mine loadMine(ConfigurationSection section) {
        String owner = section.getString("owner");
        long creationTime = section.getLong("creationTime");
        Location firstLocation = LocationHelper.locationFromString(section.getString("firstLocation"));
        Location secondLocation = LocationHelper.locationFromString(section.getString("secondLocation"));
        return new Mine(section.getName(), owner, creationTime, firstLocation, secondLocation);
    }

    public void saveMines() {
        CompletableFuture.runAsync(() -> {
            for (Mine mine : mines) {
                ConfigurationSection cs = configuration.getConfig().getConfigurationSection(mine.getName());
                cs.set("owner", mine.getOwner());
                cs.set("creationTime", mine.getCreationTime());
                cs.set("firstLocation", LocationHelper.locationToString(mine.getFirstLocation()));
                cs.set("secondLocation", LocationHelper.locationToString(mine.getSecondLocation()));
            }
            try {
                configuration.saveFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addMine(Mine mine) {
        this.mines.add(mine);
    }

    public void createMine(Mine mine) {
        ConfigurationSection section = configuration.getConfig().createSection(mine.getName());
        section.set("owner", mine.getOwner());
        section.set("creationTime", mine.getCreationTime());
        section.set("firstLocation", LocationHelper.locationToString(mine.getFirstLocation()));
        section.set("secondLocation", LocationHelper.locationToString(mine.getSecondLocation()));
        try {
            configuration.saveFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Mine> getMines() {
        return Collections.unmodifiableList(mines);
    }
}
