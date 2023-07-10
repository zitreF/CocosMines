package me.cocos.cocosmines;

import me.cocos.cocosmines.command.MineCommand;
import me.cocos.cocosmines.configuration.MineConfiguration;
import me.cocos.cocosmines.language.Language;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.language.impl.EnglishLanguage;
import me.cocos.cocosmines.language.impl.PolishLanguage;
import me.cocos.cocosmines.listener.PlayerChatListener;
import me.cocos.cocosmines.runnable.MineHologramUpdateRunnable;
import me.cocos.cocosmines.service.ArgumentService;
import me.cocos.cocosmines.service.MineService;
import me.cocos.cocosmines.service.ModificationService;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class CocosMines extends JavaPlugin {

    private static CocosMines instance;
    private MineService mineService;
    private ModificationService modificationService;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        Language language;
        if (this.getConfig().getString("language").equalsIgnoreCase("polish")) {
            language = new PolishLanguage();
        } else {
            language = new EnglishLanguage();
        }
        LanguageContainer.setLanguage(language);
        MineConfiguration mineConfiguration = new MineConfiguration(this);
        try {
            mineConfiguration.saveFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.mineService = new MineService(mineConfiguration);
        this.modificationService = new ModificationService();
        ArgumentService argumentService = new ArgumentService(this);
        this.getServer().getPluginManager().registerEvents(new PlayerChatListener(modificationService), this);
        this.getCommand("cocosmine").setExecutor(new MineCommand(argumentService));
        MineHologramUpdateRunnable mineHologramUpdateRunnable = new MineHologramUpdateRunnable(mineService);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, mineHologramUpdateRunnable, 20, 20);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, mineService::saveMines, TimeUnit.MINUTES.toSeconds(5)*20, TimeUnit.MINUTES.toSeconds(5)*20);
    }

    @Override
    public void onDisable() {
        CompletableFuture.runAsync(() -> {
            this.saveDefaultConfig();
            mineService.saveMines();
        });
    }

    public MineService getMineService() {
        return mineService;
    }

    public ModificationService getModificationService() {
        return modificationService;
    }

    public static CocosMines getInstance() {
        return instance;
    }
}
