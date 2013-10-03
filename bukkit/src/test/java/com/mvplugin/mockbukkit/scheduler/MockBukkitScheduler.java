package com.mvplugin.mockbukkit.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MockBukkitScheduler implements BukkitScheduler {

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long l) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long l, long l2) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable runnable, long l) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable runnable) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long l, long l2) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> tCallable) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancelTask(int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancelAllTasks() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isCurrentlyRunning(int i) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isQueued(int i) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<BukkitWorker> getActiveWorkers() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable runnable) throws IllegalArgumentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable) throws IllegalArgumentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long l) throws IllegalArgumentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long l) throws IllegalArgumentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long l, long l2) throws IllegalArgumentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long l, long l2) throws IllegalArgumentException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
