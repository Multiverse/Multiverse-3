package com.mvplugin.core.listeners;

import com.mvplugin.core.MultiverseCorePlugin;
import com.mvplugin.core.api.MultiverseWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class WorldListener implements Listener {

    @NotNull
    private final MultiverseCorePlugin plugin;

    public WorldListener(@NotNull final MultiverseCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void worldUnload(@NotNull final WorldUnloadEvent event) {
        if (!this.plugin.getWorldManager().isLoaded(event.getWorld().getName())) {
            return;
        }

        MultiverseWorld world = this.plugin.getWorldManager().getWorld(event.getWorld());
        if (world != null) {
            this.plugin.getEventProcessor().worldUnload(world);
        }
    }
}
