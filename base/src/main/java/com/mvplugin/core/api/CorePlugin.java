package com.mvplugin.core.api;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

public interface CorePlugin extends MultiversePlugin, PluginBase {

    @NotNull
    // Overload type
    CoreConfig config();
}
