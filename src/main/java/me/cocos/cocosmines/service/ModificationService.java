package me.cocos.cocosmines.service;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class ModificationService {

    private final Map<UUID, Consumer<AsyncPlayerChatEvent>> actions;

    public ModificationService() {
        this.actions = new HashMap<>();
    }

    public boolean contains(UUID uuid) {
        return this.actions.containsKey(uuid);
    }

    public void removeAction(UUID uuid) {
        this.actions.remove(uuid);
    }

    public Consumer<AsyncPlayerChatEvent> findAction(UUID uuid) {
        return this.actions.get(uuid);
    }

    public void addAction(UUID uuid, Consumer<AsyncPlayerChatEvent> eventConsumer) {
        this.actions.put(uuid, eventConsumer);
    }

    public Map<UUID, Consumer<AsyncPlayerChatEvent>> getAction() {
        return actions;
    }
}
