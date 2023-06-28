package me.cocos.cocosmines;

import me.cocos.cocosmines.command.MineCommand;
import me.cocos.cocosmines.configuration.MineConfiguration;
import me.cocos.cocosmines.listener.PlayerChatListener;
import me.cocos.cocosmines.menu.MainMenu;
import me.cocos.cocosmines.runnable.MineHologramUpdateRunnable;
import me.cocos.cocosmines.service.ArgumentService;
import me.cocos.cocosmines.service.MineService;
import me.cocos.cocosmines.service.ModificationService;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CocosMines extends JavaPlugin {

    private static CocosMines instance;
    private MineService mineService;
    private MainMenu mainMenu;
    private ModificationService modificationService;

    @Override
    public void onEnable() {
        instance = this;
        MineConfiguration mineConfiguration = new MineConfiguration(this);
        try {
            mineConfiguration.saveFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.mineService = new MineService(mineConfiguration);
        this.modificationService = new ModificationService();
        this.mainMenu = new MainMenu(mineService);
        ArgumentService argumentService = new ArgumentService(this);
        this.getServer().getPluginManager().registerEvents(new PlayerChatListener(modificationService), this);
        this.getCommand("cocosmine").setExecutor(new MineCommand(argumentService));
        MineHologramUpdateRunnable mineHologramUpdateRunnable = new MineHologramUpdateRunnable(mineService);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, mineHologramUpdateRunnable, 20, 20);
    }

    @Override
    public void onDisable() {
        mineService.saveMines();
    }

    public MineService getMineService() {
        return mineService;
    }

    public ModificationService getModificationService() {
        return modificationService;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public static CocosMines getInstance() {
        return instance;
    }
}
