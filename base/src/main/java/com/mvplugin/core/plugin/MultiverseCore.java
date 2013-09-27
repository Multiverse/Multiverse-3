package com.mvplugin.core.plugin;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.util.CoreConfig;
import org.jetbrains.annotations.NotNull;
import pluginbase.plugin.PluginBase;

/**
 * Multiverse 3 Core API
 * <p>
 * This API contains a bunch of useful things you can get out of Multiverse in general!
 */
public interface MultiverseCore extends MultiversePlugin, PluginBase, MultiverseCoreAPI {

    /**
     * Gets the Multiverse-Core configuration.
     *
     * @return The Multiverse-Core configuration.
     */
    @NotNull
    public CoreConfig getMVConfig();

    @NotNull
    // Overload type
    CoreConfig getSettings();
}
