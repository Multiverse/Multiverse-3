package com.mvplugin.core;

import com.mvplugin.core.api.EventProcessor;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.MultiverseWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Our default implementation of the EventProcessor.
 */
class DefaultEventProcessor implements EventProcessor {

    @NotNull
    private final MultiverseCore plugin;

    DefaultEventProcessor(@NotNull final MultiverseCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void worldUnload(@NotNull final MultiverseWorld world) {
        // TODO: unload the world from MV.
    }
}
