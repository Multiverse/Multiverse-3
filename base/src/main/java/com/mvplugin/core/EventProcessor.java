package com.mvplugin.core;

import com.mvplugin.core.plugin.MultiverseCore;
import org.jetbrains.annotations.NotNull;

/**
 * Any events that occur in minecraft that Multiverse-Core cares about should be passed into this interface.
 *
 * The events Multiverse-Core cares about will be described by the methods in this interface.
 */
final class EventProcessor {

    @NotNull
    private MultiverseCore core;

    EventProcessor(@NotNull final MultiverseCore core) {
        this.core = core;
    }

    /**
     * Call this when a world Multiverse manages WILL be unloaded by the server implementation.
     *
     * Ideally this should be called BEFORE the unload occurs but may be okay if it is called after.
     * This will let Multiverse know that it should unload the world from it's own memory.
     *
     * @param world The world being unloaded.
     */
    public void worldUnload(@NotNull final MultiverseWorld world) {
        core.getWorldManager().removeWorldFromMemory(world);
    }

    public void playerJoin(@NotNull String playerName, @NotNull String worldName) {
        MultiverseWorld world = core.getWorldManager().getWorld(worldName);
        if (world == null) {
            core.getPlayerTracker().playerLeftMultiverseWorlds(playerName);
        } else {
            core.getPlayerTracker().playerJoinedMultiverseWorld(playerName, world);
        }
    }

    public void playerQuit(@NotNull String playerName) {
        core.getPlayerTracker().playerLeftMultiverseWorlds(playerName);
    }
}
