package com.mvplugin.core;

import com.mvplugin.core.api.CoreConfig;
import com.mvplugin.core.api.CorePlugin;
import com.mvplugin.core.api.EventProcessor;
import com.mvplugin.core.api.MultiverseCore;
import com.mvplugin.core.api.WorldManager;
import org.jetbrains.annotations.NotNull;

class DefaultMultiverseCore implements MultiverseCore {

    private final CorePlugin plugin;
    private final EventProcessor eventProcessor;
    private final DefaultWorldManager worldManager;

    public DefaultMultiverseCore(@NotNull final CorePlugin plugin, @NotNull final WorldFactory worldFactory) {
        this.plugin = plugin;
        this.worldManager = new DefaultWorldManager(plugin, worldFactory);
        this.eventProcessor = new DefaultEventProcessor(plugin);
    }

    void initialize() {
        this.worldManager.initialize();
    }

    @Override
    @NotNull
    public WorldManager getWorldManager() {
        return this.worldManager;
    }

    @Override
    @NotNull
    public CoreConfig getMVConfig() {
        return getPlugin().config();
    }

    @Override
    @NotNull
    public EventProcessor getEventProcessor() {
        return this.eventProcessor;
    }

    @Override
    @NotNull
    public CorePlugin getPlugin() {
        return this.plugin;
    }
}
