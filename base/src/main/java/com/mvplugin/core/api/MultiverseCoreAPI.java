package com.mvplugin.core.api;

import com.mvplugin.core.EventProcessor;
import org.jetbrains.annotations.NotNull;

public interface MultiverseCoreAPI {

    /**
     * Gets the Multiverse world manager.
     *
     * The world manager allows you to perform various tasks related to Minecraft worlds.
     *
     * @return {@link com.mvplugin.core.api.WorldManager}.
     */
    @NotNull
    public WorldManager getWorldManager();

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
    EventProcessor getEventProcessor();

    @NotNull
    SafeTeleporter getSafeTeleporter();

    @NotNull
    BlockSafety getBlockSafety();
}
