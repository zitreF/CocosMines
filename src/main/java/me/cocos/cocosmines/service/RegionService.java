package me.cocos.cocosmines.service;

import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.data.Region;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionService {
    private final Map<String, Region> regions;

    public RegionService() {
        this.regions = new HashMap<>();
    }

    public void addMineToRegion(Mine mine) {
        Region region = new Region(mine);
        regions.put(mine.getName(), region);
    }

    public void removeMineFromRegion(Mine mine) {
        regions.remove(mine.getName());
    }

    public List<Mine> getAllMines() {
        List<Mine> mines = new ArrayList<>();
        for (Region region : regions.values()) {
            mines.add(region.getMine());
        }
        return mines;
    }

    public Region getRegionByLocation(Location location) {
        for (Region region : regions.values()) {
            if (region.isLocationWithinRegion(location)) {
                return region;
            }
        }
        return null;
    }

    public Mine findMineByName(String name) {
        Region region = regions.get(name);
        if (region != null) {
            return region.getMine();
        }
        return null;
    }
}
