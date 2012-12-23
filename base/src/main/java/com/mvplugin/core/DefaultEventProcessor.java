package com.mvplugin.core;

import com.mvplugin.core.api.CorePlugin;
import com.mvplugin.core.api.EventProcessor;
import com.mvplugin.core.api.MultiverseWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Our default implementation of the EventProcessor.
 */
class DefaultEventProcessor implements EventProcessor {

    private final CorePlugin plugin;

    DefaultEventProcessor(final CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void worldUnload(@NotNull final MultiverseWorld world) {
        // TODO: unload the world from MV.
    }
}
