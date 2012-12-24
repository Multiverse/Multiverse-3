package com.mvplugin.core;

import com.mvplugin.core.api.*;
import org.jetbrains.annotations.NotNull;

class DefaultMultiverseCore implements MultiverseCore {

    private final CorePlugin plugin;
    private final EventProcessor eventProcessor = new DefaultEventProcessor(this);
    private final WorldManager worldManager;

    public DefaultMultiverseCore(final CorePlugin plugin, final WorldFactory worldFactory) {
        this.plugin = plugin;
        this.worldManager = new DefaultWorldManager(worldFactory);
    }

    @Override
    @NotNull
    public WorldManager getWorldManager() {
        return null;
    }

    @Override
    @NotNull
    public CoreConfig getMVConfig() {
        return (CoreConfig) plugin.config();
    }

    @Override
    @NotNull
    public EventProcessor getEventProcessor() {
        return this.eventProcessor;
    }
}
