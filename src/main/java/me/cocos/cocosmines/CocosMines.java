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
import me.cocos.cocosmines.service.NotificationService;
import me.cocos.cocosmines.service.MineService;
import me.cocos.cocosmines.service.ModificationService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CocosMines extends JavaPlugin {

    private static CocosMines instance;
    private MineService mineService;
    private WorldEditPlugin worldEditPlugin;
    private final ConcurrentHashMap<String, EditSession> worldToEditSession = new ConcurrentHashMap<>();
    private ModificationService modificationService;
    private NotificationService notificationService;

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
            this.getLogger().warning("Couldn't find FastAsyncWorldEdit (FAWE) plugin. Cocosmines has been disabled!");
            this.getLogger().info("Download FAWE here: https://www.spigotmc.org/resources/fastasyncworldedit.13932/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.notificationService = new NotificationService(this.getConfig());
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
        if (notificationService.useHologram() || notificationService.useActionbar()) {
            MineHologramUpdateRunnable mineHologramUpdateRunnable = new MineHologramUpdateRunnable(mineService, notificationService);
            this.getServer().getScheduler().runTaskTimerAsynchronously(this, mineHologramUpdateRunnable, 20, 20);
        }
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, mineService::saveMines, TimeUnit.MINUTES.toSeconds(5)*20, TimeUnit.MINUTES.toSeconds(5)*20);
        MineRegenerationRunnable mineRegenerationRunnable = new MineRegenerationRunnable(mineService);
        this.getServer().getScheduler().runTaskTimer(this, mineRegenerationRunnable, 0, 20);

        this.getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                this.getLogger().info("New version of CocosMines is out: " + version + " https://www.spigotmc.org/resources/cocosmines.110860/");
            }
        });
    }

    @Override
    public void onDisable() {
        CompletableFuture.runAsync(() -> {
            this.saveDefaultConfig();
            mineService.saveMines();
        });
    }

    private void getVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 110860).openStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                consumer.accept(bufferedReader.readLine());
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException exception) {
                this.getLogger().info("Couldn't retrieve version info: " + exception.getMessage());
            }
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

    public NotificationService getHookService() {
        return notificationService;
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
