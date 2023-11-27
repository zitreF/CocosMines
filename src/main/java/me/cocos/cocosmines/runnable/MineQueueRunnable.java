package me.cocos.cocosmines.runnable;

import me.cocos.cocosmines.CocosMines;
import me.cocos.cocosmines.data.Mine;
import me.cocos.cocosmines.event.MineRegenerationEvent;
import org.bukkit.Bukkit;

import java.util.ArrayDeque;
import java.util.Deque;

public class MineQueueRunnable implements Runnable {

    private static final long BLOCKS_PER_TICK = 30_000L;
    private final Deque<Mine> minesQueue;

    public MineQueueRunnable() {
        this.minesQueue = new ArrayDeque<>();
    }

    public void queue(Mine mine) {
        this.minesQueue.offer(mine);
    }

    @Override
    public void run() {
        long blocksUpdated = 0L;

        while (!minesQueue.isEmpty() && blocksUpdated <= BLOCKS_PER_TICK) {
            Mine mine = minesQueue.poll();
            if (mine == null) break;
            MineRegenerationEvent mineRegenerationEvent = new MineRegenerationEvent(mine);
            Bukkit.getScheduler().runTask(CocosMines.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(mineRegenerationEvent));
            if (mineRegenerationEvent.isCancelled()) {
                continue;
            }
            blocksUpdated += mine.getTotalBlocks();
            mine.regenerate();
        }
    }
}
