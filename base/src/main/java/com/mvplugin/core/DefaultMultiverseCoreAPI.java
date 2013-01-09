package com.mvplugin.core;

import com.mvplugin.core.api.BlockSafety;
import com.mvplugin.core.api.MultiverseCoreAPI;
import com.mvplugin.core.api.SafeTeleporter;
import com.mvplugin.core.api.WorldManager;
import org.jetbrains.annotations.NotNull;

class DefaultMultiverseCoreAPI implements MultiverseCoreAPI {

    @NotNull
    private final WorldManager worldManager;
    @NotNull
    private final BlockSafety blockSafety;
    @NotNull
    private final EventProcessor eventProcessor;
    @NotNull
    private final SafeTeleporter safeTeleporter;

    DefaultMultiverseCoreAPI(@NotNull final WorldManager worldManager, @NotNull final BlockSafety blockSafety) {
        this.worldManager = worldManager;
        this.blockSafety = blockSafety;
        this.eventProcessor = new EventProcessor(this);
        this.safeTeleporter = new DefaultSafeTeleporter(this);
    }

    @NotNull
    @Override
    public WorldManager getWorldManager() {
        return worldManager;
    }

    @NotNull
    @Override
    public EventProcessor getEventProcessor() {
        return eventProcessor;
    }

    @NotNull
    @Override
    public SafeTeleporter getSafeTeleporter() {
        return safeTeleporter;
    }

    @NotNull
    @Override
    public BlockSafety getBlockSafety() {
        return blockSafety;
    }
}
