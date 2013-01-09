package com.mvplugin.core;

import com.mvplugin.core.api.MultiverseCoreAPI;
import com.mvplugin.core.api.MultiverseWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Any events that occur in minecraft that Multiverse-Core cares about should be passed into this interface.
 *
 * The events Multiverse-Core cares about will be described by the methods in this interface.
 */
public class EventProcessor {

    @NotNull
    private final MultiverseCoreAPI api;

    EventProcessor(@NotNull final MultiverseCoreAPI api) {
        this.api = api;
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
        this.api.getWorldManager().unloadWorld(world);
    }
}
