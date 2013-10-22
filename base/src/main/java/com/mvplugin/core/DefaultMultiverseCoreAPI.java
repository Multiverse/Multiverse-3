package com.mvplugin.core;

import com.mvplugin.core.destination.DestinationRegistry;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.BlockSafety;
import com.mvplugin.core.util.SafeTeleporter;
import org.jetbrains.annotations.NotNull;
import pluginbase.plugin.ServerInterface;

class DefaultMultiverseCoreAPI implements MultiverseCoreAPI {

    @NotNull
    private final ServerInterface serverInterface;
    @NotNull
    private final WorldManager worldManager;
    @NotNull
    private final BlockSafety blockSafety;
    @NotNull
    private final EventProcessor eventProcessor;
    @NotNull
    private final SafeTeleporter safeTeleporter;
    @NotNull
    private final DestinationRegistry destinationRegistry;
    @NotNull
    private PlayerTracker playerTracker;

    DefaultMultiverseCoreAPI(@NotNull MultiverseCore core,
            @NotNull ServerInterface serverInterface,
            @NotNull final WorldManagerUtil worldManagerUtil,
            @NotNull final BlockSafety blockSafety) {
        this.serverInterface = serverInterface;
        this.worldManager = new WorldManager(this, worldManagerUtil);
        this.blockSafety = blockSafety;
        this.eventProcessor = new EventProcessor(core);
        this.safeTeleporter = new DefaultSafeTeleporter(this);
        this.destinationRegistry = new DestinationRegistry(this);
        this.playerTracker = new PlayerTracker();
    }

    /**
     * Gets the Multiverse world manager.
     *
     * The world manager allows you to perform various tasks related to Minecraft worlds.
     *
     * @return {@link WorldManager}.
     */
    @NotNull
    @Override
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

    @NotNull
    @Override
    public DestinationRegistry getDestinationRegistry() {
        return destinationRegistry;
    }

    @NotNull
    @Override
    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    @NotNull
    @Override
    public PlayerTracker getPlayerTracker() {
        return playerTracker;
    }
}
