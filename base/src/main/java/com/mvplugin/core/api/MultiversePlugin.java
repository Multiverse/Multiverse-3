package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * Implement this if you would
 */
public interface MultiversePlugin extends PluginBase {

    /**
     * Gets the reference to MultiverseCore.
     *
     * @return A valid {@link MultiverseCore}.
     */
    @NotNull
    MultiverseCore getMultiverseCore();

    /**
     * Allows Multiverse or a plugin to query another Multiverse plugin to see what version its protocol is.
     *
     * This number should change when something will break the code.  Generally your plugin should be checking the
     * Multiverse-Core's protocol version to ensure compatibility.
     *
     * @return The protocol version.
     */
    int getProtocolVersion();
}
