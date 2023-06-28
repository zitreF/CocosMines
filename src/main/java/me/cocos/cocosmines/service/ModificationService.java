package me.cocos.cocosmines.service;

import me.cocos.cocosmines.data.Notification;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ModificationService {

    private final Map<UUID, Notification> actions;

    public ModificationService() {
        this.actions = new HashMap<>();
    }

    public boolean contains(UUID uuid) {
        return this.actions.containsKey(uuid);
    }

    public void removeAction(UUID uuid) {
        this.actions.remove(uuid);
    }

    public Notification findAction(UUID uuid) {
        return this.actions.get(uuid);
    }

    public void addAction(UUID uuid, Notification notification) {
        this.actions.put(uuid, notification);
    }

    public Map<UUID, Notification> getAction() {
        return actions;
    }
}
