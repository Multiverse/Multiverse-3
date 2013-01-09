package com.mvplugin.core;

import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.SafeTeleporter;
import com.mvplugin.core.world.WorldManager;
import org.jetbrains.annotations.NotNull;

public class MultiverseCoreAPI {

    @NotNull
    private final WorldManager worldManager;
    @NotNull
    private final BlockSafety blockSafety;
    @NotNull
    private final EventProcessor eventProcessor;
    @NotNull
    private final SafeTeleporter safeTeleporter;

    MultiverseCoreAPI(@NotNull final WorldManager worldManager, @NotNull final BlockSafety blockSafety) {
        this.worldManager = worldManager;
        this.blockSafety = blockSafety;
        this.eventProcessor = new EventProcessor(this);
        this.safeTeleporter = new DefaultSafeTeleporter(this);
    }

    /**
     * Gets the Multiverse world manager.
     *
     * The world manager allows you to perform various tasks related to Minecraft worlds.
     *
     * @return {@link com.mvplugin.core.world.WorldManager}.
     */
    @NotNull
    public WorldManager getWorldManager() {
        return worldManager;
    }

    /**
     * Gets the event processor for Multiverse-Core.
     *
     * All server implementation events that Multiverse-Core cares about are passed to this processor.
     *
     * This is mostly used internally by Multiverse only.
     *
     * @return the Multiverse-Core event processor.
     */
    @NotNull
    public EventProcessor getEventProcessor() {
        return eventProcessor;
    }

    @NotNull
    public SafeTeleporter getSafeTeleporter() {
        return safeTeleporter;
    }

    @NotNull
    public BlockSafety getBlockSafety() {
        return blockSafety;
    }
}
