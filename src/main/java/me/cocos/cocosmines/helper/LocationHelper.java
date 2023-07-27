package me.cocos.cocosmines.helper;

import me.cocos.cocosmines.data.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationHelper {

    private LocationHelper() {}

    public static String locationToString(Location location) {
        StringBuilder builder = new StringBuilder();
        return builder.append(location.getWorld().getName())
                .append(":")
                .append(location.getBlockX())
                .append(":")
                .append(location.getBlockY())
                .append(":")
                .append(location.getBlockZ()).toString();
    }

    public static Location locationFromString(String string) {
        String[] splitted = string.split(":");
        return new Location(Bukkit.getWorld(splitted[0]),
                Double.parseDouble(splitted[1]),
                Double.parseDouble(splitted[2]),
                Double.parseDouble(splitted[3]));
    }

    public static boolean isLocationWithinRegion(Mine mine, Location location) {
        Location firstLocation = mine.getFirstLocation();
        Location secondLocation = mine.getSecondLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return (x >= firstLocation.getBlockX() && x <= secondLocation.getBlockX())
                && (y >= firstLocation.getBlockY() && y <= secondLocation.getBlockY())
                && (z >= firstLocation.getBlockZ() && z <= secondLocation.getBlockZ());
    }
}
