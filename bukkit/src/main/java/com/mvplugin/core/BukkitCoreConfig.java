package com.mvplugin.core;

import com.mvplugin.core.util.CoreConfig;
import org.jetbrains.annotations.NotNull;
import pluginbase.bukkit.properties.YamlProperties;
import pluginbase.logging.Logging;
import pluginbase.messages.PluginBaseException;

import java.io.File;

/**
 * A yaml implementation of Multiverse-Core's primary configuration file.
 */
class BukkitCoreConfig extends YamlProperties implements CoreConfig {

    BukkitCoreConfig(@NotNull final MultiverseCoreBukkitPlugin plugin) throws PluginBaseException {
        super(Logging.getLogger(), true, true, new File(plugin.getDataFolder(), "config.yml"), CoreConfig.class);
    }
}
