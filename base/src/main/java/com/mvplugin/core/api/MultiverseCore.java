package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * Multiverse 3 Core API
 * <p>
 * This API contains a bunch of useful things you can get out of Multiverse in general!
 */
public interface MultiverseCore extends MultiversePlugin, PluginBase {

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
     * Gets the Multiverse-Core configuration.
     *
     * @return The Multiverse-Core configuration.
     */
    @NotNull
    public CoreConfig getMVConfig();

    @NotNull
    // Overload type
    CoreConfig config();

    /**
     * Gets the event processor for Multiverse-Core.
     *
     * All server implementation events that Multiverse-Core cares about are passed to this processor.
     *
     * @return the Multiverse-Core event processor.
     */
    @NotNull
    EventProcessor getEventProcessor();
}
