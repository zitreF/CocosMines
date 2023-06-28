package me.cocos.cocosmines.data;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public final class Notification {

    private final String notification;
    private final Consumer<AsyncPlayerChatEvent> consumer;

    public Notification(String notification, Consumer<AsyncPlayerChatEvent> consumer) {
        this.notification = notification;
        this.consumer = consumer;

    }

    public String getNotification() {
        return notification;
    }

    public Consumer<AsyncPlayerChatEvent> getConsumer() {
        return consumer;
    }
}
