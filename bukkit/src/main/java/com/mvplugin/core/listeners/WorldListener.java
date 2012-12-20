package com.mvplugin.core.listeners;

import com.mvplugin.core.api.MultiverseCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class WorldListener implements Listener {

    @NotNull
    private final MultiverseCore plugin;

    public WorldListener(@NotNull final MultiverseCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void worldUnload(@NotNull final WorldUnloadEvent event) {
        if (!this.plugin.getWorldManager().isMVWorld(event.getWorld().getName())) {
            //return;
        }

        // TODO: pass this to EventProcessor once we can get MV worlds.
    }
}
