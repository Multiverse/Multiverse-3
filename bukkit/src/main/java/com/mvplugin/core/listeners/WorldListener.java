package com.mvplugin.core.listeners;

import com.mvplugin.core.MultiverseCorePlugin;
import com.mvplugin.core.api.MultiverseWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

    private final MultiverseCorePlugin plugin;

    public WorldListener(final MultiverseCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void worldUnload(final WorldUnloadEvent event) {
        if (!this.plugin.getWorldManager().isManaged(event.getWorld().getName())) {
            return;
        }

        MultiverseWorld world = this.plugin.getWorldManager().getWorld(event.getWorld());
        this.plugin.getEventProcessor().worldUnload(world);
    }
}
