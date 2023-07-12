package me.cocos.cocosmines;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.world.World;
import me.cocos.cocosmines.command.MineCommand;
import me.cocos.cocosmines.configuration.MineConfiguration;
import me.cocos.cocosmines.language.Language;
import me.cocos.cocosmines.language.LanguageContainer;
import me.cocos.cocosmines.language.impl.EnglishLanguage;
import me.cocos.cocosmines.language.impl.PolishLanguage;
import me.cocos.cocosmines.listener.PlayerChatListener;
import me.cocos.cocosmines.runnable.MineHologramUpdateRunnable;
import me.cocos.cocosmines.runnable.MineRegenerationRunnable;
import me.cocos.cocosmines.service.ArgumentService;
import me.cocos.cocosmines.service.MineService;
import me.cocos.cocosmines.service.ModificationService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CocosMines extends JavaPlugin {

    private static CocosMines instance;
    private MineService mineService;
    private WorldEditPlugin worldEditPlugin;
    private final ConcurrentHashMap<String, EditSession> worldToEditSession = new ConcurrentHashMap<>();
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
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            Logger.getLogger("cocosmines").log(Level.WARNING, "Couldn't find FastAsyncWorldEdit (FAWE) plugin. Cocosmines has been disabled!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        FaweAPI.getTaskManager().repeatAsync(this::handleEditSessions, 20);
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
        MineRegenerationRunnable mineRegenerationRunnable = new MineRegenerationRunnable(mineService);
        this.getServer().getScheduler().runTaskTimer(this, mineRegenerationRunnable, 0, 20);
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

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public ModificationService getModificationService() {
        return modificationService;
    }

    public static CocosMines getInstance() {
        return instance;
    }

    private void handleEditSessions() {
        for (EditSession session : worldToEditSession.values()) {
            if (session.size() > 0) {
                session.close();
            }
        }
        worldToEditSession.clear();
    }

    public EditSession getEditSession(World world) {
        if (!worldToEditSession.containsKey(world.getId())) {
            EditSession session = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
            session.setReorderMode(EditSession.ReorderMode.FAST);
            worldToEditSession.put(world.getId(), session);
        }

        return worldToEditSession.get(world.getId());
    }
}
