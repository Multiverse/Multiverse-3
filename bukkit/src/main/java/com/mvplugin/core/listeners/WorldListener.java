package com.mvplugin.core.listeners;

import com.mvplugin.core.api.CorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

    private final CorePlugin plugin;

    public WorldListener(final CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void worldUnload(final WorldUnloadEvent event) {
        if (!this.plugin.getWorldManager().isMVWorld(event.getWorld().getName())) {
            return;
        }

        // TODO: pass this to EventProcessor once we can get MV worlds.
    }
}
