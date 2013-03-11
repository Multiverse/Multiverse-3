package com.mvplugin.core.listeners;

import com.mvplugin.core.MultiverseCoreBukkitPlugin;
import com.mvplugin.core.world.MultiverseWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class BukkitWorldListener implements Listener {

    @NotNull
    private final MultiverseCoreBukkitPlugin plugin;

    public BukkitWorldListener(@NotNull final MultiverseCoreBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void worldUnload(@NotNull final WorldUnloadEvent event) {
        if (!this.plugin.getWorldManager().isLoaded(event.getWorld().getName())) {
            return;
        }

        final MultiverseWorld world = this.plugin.getWorldManager().getWorld(event.getWorld().getName());
        if (world != null) {
            this.plugin.getEventProcessor().worldUnload(world);
        }
    }
}
