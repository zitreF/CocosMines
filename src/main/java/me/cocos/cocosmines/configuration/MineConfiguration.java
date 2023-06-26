package me.cocos.cocosmines.configuration;

import me.cocos.cocosmines.CocosMines;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class MineConfiguration {

    private final File file;
    private final FileConfiguration config;

    public MineConfiguration(CocosMines plugin) {
        this.file = new File(plugin.getDataFolder(), "mines.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveFile() throws IOException {
        this.config.save(file);
    }
}
