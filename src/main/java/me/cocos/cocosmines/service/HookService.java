package me.cocos.cocosmines.service;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.placeholder.PlaceholderReplacer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

public final class HookService {

    private final boolean useActionbar;
    private boolean useHologram;

    public HookService(FileConfiguration config) {
        this.setup("DecentHolograms", () -> this.useHologram = config.getBoolean("notification.useHologram"));
        this.setup("PlaceholderAPI", () -> new PlaceholderReplacer().register());
        this.useActionbar = config.getBoolean("notification.useActionbar");
    }

    private void setup(String pluginName, Runnable hook) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (pluginManager.isPluginEnabled(pluginName)) {
            hook.run();
        }
    }

    public boolean useActionbar() {
        return useActionbar;
    }

    public boolean useHologram() {
        return useHologram;
    }
}
