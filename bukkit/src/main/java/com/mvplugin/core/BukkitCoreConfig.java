package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.bukkit.properties.YamlProperties;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.mvplugin.core.util.CoreConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A yaml implementation of Multiverse-Core's primary configuration file.
 */
class BukkitCoreConfig extends YamlProperties implements CoreConfig {

    BukkitCoreConfig(@NotNull final MultiverseCoreBukkitPlugin plugin) throws PluginBaseException {
        super(Logging.getLogger(), true, true, new File(plugin.getDataFolder(), "config.yml"), CoreConfig.class);
    }
}
